package edu.utexas.testera.translator;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import edu.mit.csail.sdg.alloy4.Err;
import edu.mit.csail.sdg.alloy4.SafeList;
import edu.mit.csail.sdg.alloy4compiler.ast.Expr;
import edu.mit.csail.sdg.alloy4compiler.ast.Sig;
import edu.mit.csail.sdg.alloy4compiler.ast.Sig.Field;
import edu.mit.csail.sdg.alloy4compiler.translator.A4Solution;
import edu.mit.csail.sdg.alloy4compiler.translator.A4Tuple;
import edu.mit.csail.sdg.alloy4compiler.translator.A4TupleSet;

public class Instance2Java {

	protected HashMap<String, Object> objects; // maps valuations to java
												// instances

	protected HashSet<String> ignoreList = new HashSet<String>(); // stores the
																	// list of
																	// signatures
																	// to ignore
	protected HashSet<String> needtoImport = new HashSet<String>(); // stores
																	// the full
																	// path of
																	// classes
																	// used in
																	// the
																	// generated
																	// tests
	// Stores the receiver object of the method under test
	protected String receiverObject;
	// Stores the name of the method under test
	protected String methodName;
	// Stores the list of parameters
	protected List<String> paras = new ArrayList<String>();
	// Stores the list name to object mapping created
	protected List<String> n2o = new ArrayList<String>();

	protected boolean isStatic;

	protected boolean isVoid;

	public Instance2Java(boolean isStatic, boolean isVoid) {
		objects = new HashMap<String, Object>();
		this.isStatic = isStatic;
		this.isVoid = isVoid;
	}

	public HashSet<String> getImport() {
		return needtoImport;
	}

	public HashMap<String, Object> getObjects() {
		return objects;
	}

	public String getReveiverObject() {
		return receiverObject;
	}

	public String getMethodName() {
		return methodName;
	}

	public List<String> getParas() {
		return paras;
	}

	public List<String> getN2o() {
		return n2o;
	}

	public void initialize() {
		ignoreList.add("seq");
		//ignoreList.add("this");

	}

	public List<String> generateJavaCode(A4Solution ans, boolean createObjects)
			throws Exception {
		initialize();
		List<String> initStats = new ArrayList<String>();
		initStats.add("// TestEra Auto-Comment: Initialization statements");
		// StringBuffer sb = new StringBuffer();
		SafeList<Sig> allReachableSigs = ans.getAllReachableSigs();

		// Store parameters into paras
		for (Expr e : ans.getAllSkolems()) {
			// System.out.println("e:"+e);
			String para = getParaFromExpr(e);
			methodName = getMethFromExpr(e);
			A4TupleSet tuples = (A4TupleSet) ans.eval(e);
			for (A4Tuple tuple : tuples) {
				paras.add(getShortTuple(tuple));
				// System.out.println("skolem:"+tuple+"-"+getShortTuple(tuple));
			}
		}

		if (!isVoid)
			paras.remove(paras.size() - 1);

		// Store receiver object to receiverObject
		if (!isStatic)
			receiverObject = paras.remove(0);
		else
			receiverObject = "";

		//System.out.println("paras.size():"+paras.size());
		// create all objects
		for (Sig s : allReachableSigs) {
			// check if it is a java object to be created
			 //System.out.println("s.label:"+s.label);
			if (isDefinedClass(s.label)) {
				String fullClassName = getClassFullPath(s.label);
				needtoImport.add(fullClassName);

				String shortClassName = fullClassName.substring(fullClassName
						.lastIndexOf(".") + 1);

				// create all instances of that class
				A4TupleSet tuples = ans.eval(s);
				for (A4Tuple tuple : tuples) {
					// System.out.println("tuple:"+tuple);
					String shortTuple = getShortTuple(tuple);
					if (shortClassName.equals("boolean")) {
						initStats.add(shortClassName + " " + shortTuple
								+ " = " + getBooleanValue(shortTuple) + ";");
					} else if (shortClassName.equals("Int")) {
						

					} else {
						initStats.add(shortClassName + " " + shortTuple
								+ " = new " + shortClassName + "();");
						// add created objects' names to the o2n list
						n2o.add(shortTuple);
					}

					if (createObjects) {
						try {
							Class c = Class.forName(fullClassName);
							objects.put(tuple.toString(), c.newInstance());
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}
		// assign the fields to the objects created
		for (Sig s : allReachableSigs) {
			// check if the java object is supported
			if (isDefinedClass(s.label)) {
				// assign the value of each field
				// we assume same name of fields are used by alloy
				for (Field f : s.getFields()) {
					// System.out.println("f.label:" + f.label);
					// check if the field is to be converted to a java field
					// this is just necessary in case there are additional
					// fields used in the alloy model
					// which are independent of Java object fields
					A4TupleSet tuples = ans.eval(f);
					for (A4Tuple tuple : tuples) {
						String objName = getShortTuple(tuple.atom(0));
						String fieldName = f.label;
						if (tuple.arity() == 4) {
							String rightValue = toJavaValue(tuple.atom(2));
							String state = tuple.atom(3);
							// System.out.println("state:"+state);
							if (state.endsWith("Pre$0")) {
								initStats.add(objName + "." + fieldName + "["
										+ tuple.atom(1) + "] = " + rightValue
										+ ";");

								if (createObjects) {
									try {
										Object o = objects.get(objName);
										Object arrayField = o.getClass()
												.getField(fieldName).get(o);
										Object value;
										try {
											value = Integer
													.parseInt(rightValue);
										} catch (NumberFormatException e) {
											value = objects.get(rightValue);
										}
										Array.set(
												arrayField,
												Integer.parseInt(tuple.atom(1)),
												value);
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							}
						} else {
							String rightValue = toJavaValue(tuple.atom(1));

							String state = tuple.atom(2);
							// System.out.println("state:" + state);
							if (state.endsWith("Pre$0")) {
								initStats.add(objName + "." + fieldName + " = "
										+ rightValue + ";");
								// sb.append(state + "\n");
								if (createObjects) {
									try {
										Object o = objects.get(objName);
										Object value;
										try {
											value = Integer
													.parseInt(rightValue);
										} catch (NumberFormatException e) {
											value = objects.get(rightValue);
										}
										o.getClass().getField(fieldName)
												.set(o, value);
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							}
						}
					}
				}
			}
		}
		return initStats;
	}

	public boolean isDefinedClass(String label) {
		// System.out.println("label:" + label);
		String[] items = label.split("/");
		if (items.length < 2)
			return false;
		String fullPath = items[items.length - 2];
		if (fullPath.startsWith("util_")) {
			return false;
		} else {
			for (String toIgnore : ignoreList) {
				if (toIgnore.equals(fullPath))
					return false;
			}
		}
		return true;
	}

	public String getClassFullPath(String label) {
		String[] items = label.split("/");
		String fullPath = items[items.length - 2];
		return fullPath.replace("_", ".");
	}

	public String getShortTuple(A4Tuple tuple) {
		String name = tuple.toString();
		if (name.contains("/"))
			name = name.substring(name.lastIndexOf("/") + 1);
		name = name.replace("$", "_");
		
		return toJavaValue(name);
	}

	public String getShortTuple(String tuple) {
		String name = tuple.toString();
		if (name.contains("/"))
			name = name.substring(name.lastIndexOf("/") + 1);
		name = name.replace("$", "_");
		return toJavaValue(name);
	}

	public String toJavaValue(String value) {
		value=value.replace("$", "_");
		if (value.endsWith("False_0"))
			return "false";
		if (value.endsWith("True_0"))
			return "true";
		if (value.contains("/")) {
			return value.substring(value.lastIndexOf("/") + 1);
		}
		return value;

	}
	
	public String getBooleanValue(String shortTuple){
		if(shortTuple.startsWith("True"))
			return "true";
		else return "false";
	}

	public String getParaFromExpr(Expr e) {
		String eStr = e.toString();
		return eStr.substring(eStr.lastIndexOf("_") + 1);
	}

	public String getMethFromExpr(Expr e) {
		String eStr = e.toString();
		return eStr.substring(1, eStr.indexOf("_"));
	}
}
