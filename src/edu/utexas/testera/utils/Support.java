package edu.utexas.testera.utils;

import edu.mit.csail.sdg.alloy4compiler.ast.Sig;

public class Support 
{
	/**
	 *  Not all classes are supported, which is not a good thing.
	 *  The use of isEnabled is false is preferred for the time being.
	 *  
	 *  Need to differentiate between classes declared by the user and those in the  JDK.
	 * @param t
	 * @return
	 */
	public static boolean isClassSupported(Class t)
	{
		if (t.isPrimitive())
			return true;
		if (String.class == t)
			return true;
		return true;
	}
	
	public static boolean isAlloyPrimitive(Class t)
	{
		if (t.isArray()){
			return isAlloyPrimitive(t.getComponentType());
		}
		return t==int.class || t==boolean.class || Integer.class==t || Boolean.class ==t 
		|| String.class == t ;
	}
	
	public static boolean isAlloyPrimiive(Sig sig)
	{
		if (sig==null)
			return false;
		if (sig.label.equalsIgnoreCase("Int") || sig.label.equalsIgnoreCase("String")
				|| sig.label.startsWith("boolean/"))
			return true;
		return false;
	}
	
	public static String getAlloyName(Class t){
		if(t.isPrimitive()){
			if(t==int.class || t==Integer.class){
				return "Int";
			}else if (t==boolean.class || t==Boolean.class){
				return "Bool";
			}else{
				//TODO: other types, e.g., float, double, ...
				return "Int";
			}
		}else if(t.isArray()){
			return "seq " + getAlloyName(t.getComponentType());
		}else{
			return t.getSimpleName();
		}
	}
}
