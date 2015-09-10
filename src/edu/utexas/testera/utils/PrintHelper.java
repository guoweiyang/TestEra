package edu.utexas.testera.utils;

public class PrintHelper 
{
	/**
	 * Returns a string representation of alloy fact section including all facts in the facts array.
	 * If unique is true the name of the fact is the name + unique number
	 * @param name the name of the fact, should not contain spaces or dots
	 * @param facts
	 * @param unique
	 * @return
	 */
	public static String printFacts(String name, String[] facts, boolean unique)
	{
		if (name.indexOf(' ') >-1 || name.indexOf('.') >-1)
			throw new IllegalArgumentException("The name of the fact should not contain spaces or dots.");
		StringBuffer sb = new StringBuffer();
		for (int i=0; facts!=null && i<facts.length; i++)
		{
			if (i==0) sb.append("fact " + name + (unique? System.currentTimeMillis() : "") + " { \n ");
			sb.append("\t" + facts[i]);
			sb.append("\n");
			if (i==facts.length-1) sb.append("}\n");
		}
		return sb.toString();
	}
}
