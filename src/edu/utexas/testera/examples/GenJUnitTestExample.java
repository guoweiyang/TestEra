package edu.utexas.testera.examples;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import dataStructures.list.LinkedList;


import edu.mit.csail.sdg.alloy4compiler.translator.A4Solution;
import edu.utexas.testera.alloyComponents.AlloyModel;
import edu.utexas.testera.api.GenerateAlloyInput;
import edu.utexas.testera.api.GenerateJUnitTests;
import edu.utexas.testera.main.MainTester;
import edu.utexas.testera.translator.JavaToAlloy;

public class GenJUnitTestExample {
	public static void main(String[] args){
		GenJUnitTestExample ex = new GenJUnitTestExample();
		ex.gen();
	}
	
	public void gen() {	
		int limit = 10;
		Class c = LinkedList.class;

		// PATH TO STORE THE RESULTS
		String path = "results/";
		
		try {
			Method m = LinkedList.class.getDeclaredMethod("addNode", int.class);
			java.util.List<String> paras = new ArrayList<String>();
			paras.add("x");
		
			GenerateJUnitTests.run(c, m, path, paras, limit);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
