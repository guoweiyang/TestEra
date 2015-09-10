package edu.utexas.testera.io;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import edu.mit.csail.sdg.alloy4compiler.translator.A4Solution;
import edu.utexas.testera.translator.Instance2Java;

public class JUnitGenerator {
	String packName;
	String testName;
	// stores full paths of all the classes that need to be imported
	HashSet<String> toImport = new HashSet<String>();
	// stores the mapping between test names and test contents
	HashMap<String, List<String>> tests = new HashMap<String, List<String>>();

	protected boolean isStatic = false;
	protected boolean isVoid = false;
	protected Class c;
	protected Method m;

	public JUnitGenerator(Class c, Method m, String path) {
		setPackage(c.getName());
		setTestName(m.getName()+"Test");
		toImport.add("org.junit.Test");
		toImport.add("edu.utexas.testera.translator.StateManager");
		toImport.add("edu.utexas.testera.core.TestEra");
		this.c = c;
		this.m = m;
		// Check if the method under test is static
		if (m.getModifiers() == Modifier.STATIC)
			isStatic = true;
		// Check if the method under test is void
		if (m.getReturnType() == (Void.TYPE))
			isVoid = true;
	}
	
	public void setPackage(String packName){
		this.packName=packName+"Test";
	}
	public void setTestName(String testName){
		this.testName=testName;
	}

	// Generate JUnit tests
	public void genJUnitTests(A4Solution ans, int limit) throws Exception {
		int counter = 0;
		counter = 0;
		while (ans != null && ans.satisfiable()) {
			counter++;
			if (counter > limit)
				break;

			// Concretization from Alloy instances to Java initializations
			Instance2Java i2j = new Instance2Java(isStatic, isVoid);
			List<String> stats = i2j.generateJavaCode(ans, false);
			toImport.addAll(i2j.getImport());
			// Store the object list, method name, parameter list, and receriver
			// object
			List<String> n2o = i2j.getN2o();
			String methodName = i2j.getMethodName();
			List<String> paras = i2j.getParas();
			String receiverObject = i2j.getReveiverObject();

			// Add pre-state abstraction
			stats.add("// TestEra Auto-Comment: Pre-state abstraction");
			addPreAbstraction(stats, n2o);

			// Add invocation of method under test
			stats.add("// TestEra Auto-Comment: Invoke method under test");
			String methodInvoke = addMethodUnderTest(receiverObject,
					methodName, paras);
			stats.add(methodInvoke);

			// Add post-state abstraction
			stats.add("// TestEra Auto-Comment: Post-state checking");
			addPostChecking(stats, c, methodName, receiverObject, paras);

			// Add statements to tests
			String testName = "test" + counter;

			addToTests(testName, stats);
			ans = ans.next();
		}
	}

	// Add pre-state abstraction to JUnit tests
	public void addPreAbstraction(List<String> stats, List<String> n2o) {
		stats.add("StateManager sm = new StateManager();");
		for (String o : n2o) {
			stats.add("sm.addToState(\"" + o + "\", " + o + ");");
		}
		stats.add("sm.generatePreState();");
	}

	// Add invocation of method under test to JUnit tests
	public String addMethodUnderTest(String receiverObject, String methodName,
			List<String> paras) {

		String methodInvoke = "";
		if (!isVoid) {
			methodInvoke += m.getReturnType().getName() + " result = ";
		}
		if (!isStatic) {
			methodInvoke += receiverObject + ".";
		}

		methodInvoke += methodName + "(";
		for (String para : paras) {
			methodInvoke += para + ",";
		}
		if (methodInvoke.endsWith(","))
			methodInvoke = methodInvoke.substring(0, methodInvoke.length() - 1);
		methodInvoke += ");";

		return methodInvoke;
	}

	// Add post-state checking to JUnit tests
	public void addPostChecking(List<String> stats, Class c, String methodName,
			String receiverObject, List<String> paras) {
		String postChecking = "TestEra.checkPostState(sm, \"" + c.getName()
				+ "\", \"" + methodName + "\", \"";
		if (!isStatic)
			postChecking += receiverObject + "\", ";
		for (String para : paras) {
			postChecking += "\"" + para + "\", ";
		}
		if (!isVoid) {
			Class rt = m.getReturnType();
			if (rt.isPrimitive()) {
				postChecking += "result+\"\", ";
			} else
				postChecking += "\"result\", ";
		}
		if (postChecking.endsWith(", ")) {
			postChecking = postChecking.substring(0, postChecking.length() - 2);
		}
		postChecking += ");";
		stats.add(postChecking);
	}

	public void addToImport(HashSet<String> set) {
		toImport.addAll(set);
	}

	public void addToTests(String testName, List<String> stats) {
		if (!tests.containsKey(testName))
			tests.put(testName, stats);
		else
			tests.get(testName).addAll(stats);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("package "+packName+";\n");
		for (String item : toImport) {
			sb.append("import " + item + ";\n");
		}
		sb.append("\n");
		sb.append("public class "+testName+" {\n");
		for (String testName : tests.keySet()) {
			List<String> testContent = tests.get(testName);
			sb.append("\t@Test\n");
			sb.append("\tpublic void " + testName + "() {\n");
			for (String line : testContent) {
				sb.append("\t\t" + line + "\n");
			}
			sb.append("\t}\n");

		}
		sb.append("}\n");
		return sb.toString();

	}
}
