package edu.utexas.testera.tests;

import edu.utexas.testera.annotations.TestEra;

public class Test {
	String  elem;
	
	@TestEra (
			runCommand = "run TestEra for exactly 1 list",
			invariants = "fact bla {}"
	)
	
	public void print(Object e)
	{
		System.out.println("hello test " + e);
	}
	
	public void print_2(Object e)
	{
		System.out.println("hello test2 " + e);
	}
	
	class TestNode{
		int elem;
		TestNode next;
	}
	
	public static void main(String args[]) {
		List l = new List();
		Node n = new Node();
		
	}
}
