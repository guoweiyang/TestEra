package edu.utexas.testera.tests;

import java.lang.reflect.Method;

import edu.utexas.testera.annotations.TestEra;

@TestEra (invariants={"all n:List.header.*next | n !in n.^next"})
public class List {
	public Node header;
	
	@TestEra (runCommand="1 List, 3 Node, 2 int")
	public void removeBoolean(boolean x) {

	}
	
	@TestEra (runCommand="1 List, 3 Node, 2 int")
	public void removeInt(int x) {

	}
	
	@TestEra (runCommand="1 List, 3 Node, 2 int")
	public void remove(Node x) {

	}
	
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		if (header==null)
			return "Empty List";
		sb.append("header : " + header);
		Node n = header.next;
		while (n!=null)
		{
			sb.append(" -> " + n);
			n = n.next;
		}
		return sb.toString();
	}
	
	public static void main(String args[]) throws Exception{
		Method m = List.class.getMethod("remove", boolean.class);
		
		System.out.println(m.getReturnType().equals(Void.class));
	}
}
