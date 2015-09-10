package edu.utexas.testera.tests;

import java.lang.reflect.Method;
import java.util.ArrayList;

import dataStructures.list.LinkedList;
import edu.utexas.testera.main.MainTester;

public class MainTester_test {

	public static void main(String[] args) throws Exception {
		int limit = 10;
		Class c = LinkedList.class;
		Method m = LinkedList.class.getDeclaredMethod("addNode", int.class);
		// Method m = List.class.getDeclaredMethod("remove", Node.class);
		java.util.List<String> paras = new ArrayList<String>();
		paras.add("x");
		String parentPath = "";
		MainTester.test(c, m, parentPath, paras, limit);
	}

}
