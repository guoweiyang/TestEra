package edu.utexas.testera.alloyComponents;

import edu.utexas.testera.utils.PrintHelper;

import edu.mit.csail.sdg.alloy4.SafeList;
import edu.mit.csail.sdg.alloy4compiler.ast.Sig;
import edu.mit.csail.sdg.alloy4compiler.ast.Sig.Field;
import edu.mit.csail.sdg.alloy4compiler.ast.Sig.PrimSig;

/**
 * Stores an Alloy signature and relates it to a corresponding Java class under test.
 * Information about the signature are stored in the alloy.sig object (such as the fields).
 * 
 * @author Shadi
 *
 */
public class Signature 
{
	private PrimSig sig;
	//the Java class from which this signature might have been created
	private Class clazz;
	//the Java class name from which this signature might have been created
	private String className;
	//the simple Java class name, eg: className=java.lang.String, classSimpleName=String
	private String classSimpleName;
	//list of facts associated to this signature, these facts can be written using TestEra(facts={}) annotations for the Java class
	//defined for this siganture
	private String[] facts;
	//in case this signature is one of alloy util pre-defined sigs, then this path is added as open path at the top of the als file
	//private String alloyUtilPath;
	//the label of the sig, this is usually the Java class name replacing any dots with '_' (underscore,
	//it can also be one of alloy primitive sigs with different names than the corresponding Java class name
	private String label;
	
	public void setClassName(String className) {
		this.className = className;
	}
	public String getClassName() {
		return className;
	}
	public void setFacts(String[] facts) {
		this.facts = facts;
	}
	public String[] getFacts() {
		return facts;
	}
	public void setSig(PrimSig sig) {
		this.sig = sig;
	}
	public PrimSig getSig() {
		return sig;
	}
	//the label is the name of the sig used in the alloy model
	//it is the same as the class name replacing any dots with s_ (underscore)
	public String getLabel() {
		if (label != null)
			return label;
		if (classSimpleName==null)
			return null;
		return classSimpleName.replace('.', '_');
	}
	
	public void setLabel(String label)
	{
		this.label = label;
	}
	public void setClassSimpleName(String classSimpleName) {
		this.classSimpleName = classSimpleName;
	}
/*	public void setAlloyUtilPath(String alloyUtilPath) {
		this.alloyUtilPath = alloyUtilPath;
	}
	public String getAlloyUtilPath() {
		return alloyUtilPath;
	}
	*/
	public int hashCode()
	{
		return className.hashCode();
	}
	
	public boolean equals(Object o)
	{
		if (! (o instanceof Signature))
			return false;
		Signature sig = (Signature)o;
		return className.equals(sig.getClassName());
	}
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		SafeList<Field> fields = sig.getFields();
		if (sig.isOne != null)
			sb.append("one ");
		else if (sig.isAbstract != null) 
			sb.append("abstract ");
		sb.append("sig "+ getLabel() + " ");
		if (sig.parent != null && sig.parent != Sig.UNIV)
			sb .append("extends "+ sig.parent.label + " ");
		sb.append("{"); 
		
		boolean first = true;
		for (Field f: fields)
		{
			if (!first) sb.append(",");
            first=false;
            sb.append( "\n\t" + f.label + " : " );
            if (isArray(f)) {
            	sb.append( "(seq " );
            	String s = f.decl().expr.toString();
            	String[] words = s.split(" ");
            	for (int i=0; i<words.length; i++) {
            		sb.append(words[i] + " ");
            		if (i==0) {
            			sb.append(")");
            		}
            	}
            }
            else 
            	sb.append(f.decl().expr);
		}
		sb.append("\n}\n");
		sb.append(PrintHelper.printFacts(getLabel()+ "_fact" , facts, false));
		return sb.toString();
	}
	private boolean isArray(Field f) {
		for (java.lang.reflect.Field f_c : clazz.getDeclaredFields()) {
			if (f_c.getName().equalsIgnoreCase(f.label)) {
				return f_c.getType().isArray();
			}
		}
		return false;
	}
	
	/**
	 * @param clazz the clazz to set
	 */
	public void setClass(Class clazz) {
		this.clazz = clazz;
	}
	/**
	 * @return the java class which this sig represents
	 */
	public Class getClassDef() {
		return clazz;
	}
}
