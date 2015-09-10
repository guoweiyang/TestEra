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
import edu.utexas.testera.main.MainTester;
import edu.utexas.testera.translator.JavaToAlloy;
import edu.utexas.testera.visualizer.InstanceVisualizer;

public class GenAlloyInputExample {
	public static void main(String[] args){
		GenAlloyInputExample ex = new GenAlloyInputExample();
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

			A4Solution ans = GenerateAlloyInput.run(c, m, path, paras, limit);
			InstanceVisualizer iv = new InstanceVisualizer();
			iv.visualize(ans);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
