package edu.utexas.testera.main;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;

import dataStructures.list.LinkedList;
import edu.mit.csail.sdg.alloy4compiler.translator.A4Solution;
import edu.utexas.testera.alloyComponents.AlloyModel;
import edu.utexas.testera.io.FileGenerator;
import edu.utexas.testera.io.JUnitGenerator;
import edu.utexas.testera.translator.AlloyTestGenerator;
import edu.utexas.testera.translator.JavaToAlloy;
import edu.utexas.testera.utils.Constants;

public class MainTester {

	public static void test(Class c, Method m, String parentPath,
			java.util.List<String> paras, int limit) throws Exception {
		JavaToAlloy j2a = new JavaToAlloy();
		Set<AlloyModel> models = j2a.translateClasses2Sigs(c, m);

		FileGenerator fg = new FileGenerator(Constants.alloyOut);
		Constants.parentPath=parentPath;
		fg.setFileParentPath(Constants.parentPath + Constants.alloyOut);
		fg.generateFiles(models);

		AlloyTestGenerator atg = new AlloyTestGenerator();
		atg.generatePreTestModel(c, m, paras, j2a.getFieldNames());

		fg.generateFile(atg.postModel);
		String fileName = fg.generateFile(atg.preModel);

		// Run and get the Alloy models
		String modelPath = fileName; // hard-coded for testing

		A4Solution sln = AlloyRunner.run(modelPath);

		// Initialize the JUnit generator, take a path as the parameter
		JUnitGenerator junitGen = new JUnitGenerator(c, m, "");
		

		// Transform Alloy instances into JUnit initialization statements and
		// add method invocation as well as pre-state abstraction and post-state
		// checking
		junitGen.genJUnitTests(sln, limit);

		// Output JUnit tests to file system
		FileGenerator junit = new FileGenerator(Constants.junitOut);
		junit.setFileParentPath(Constants.parentPath + Constants.junitOut);
		String path = c.getName().replace(".", "/") + "Test/" + m.getName();
		
		junit.generateFile(path, junitGen.toString());
		//System.out.println(junitGen.toString());
	}
}
