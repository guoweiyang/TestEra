package edu.utexas.testera.alloyComponents;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import edu.mit.csail.sdg.alloy4.Err;
import edu.mit.csail.sdg.alloy4compiler.ast.Sig;
import edu.mit.csail.sdg.alloy4compiler.ast.Sig.PrimSig;

public class AlloyModel 
{
	static Logger logger = Logger.getLogger(AlloyModel.class.getName());

	//user defined sigs generated through user defined java classes
	//the key is the complete java class name
	protected HashMap<String, Signature> sigs;
	//primitive signatures, basically those found in models/util/ within alloy bundle 
	private HashMap<String, Signature> primitiveSigs;
	private List<String> facts = new LinkedList<String>();
	private String moduleName;
	private HashSet<String> dependencies = new HashSet<String>();
	private HashSet<Predicate> predicates = new HashSet<Predicate>();
	private String command = "";
	private List<String> bodyStmts = new LinkedList<String>();
	
	public AlloyModel() {
		sigs = new HashMap<String, Signature>();
		primitiveSigs = new HashMap<String, Signature>();
	}
	public AlloyModel(HashMap<String, Signature> sigSet) {
		sigs = sigSet;
		primitiveSigs = new HashMap<String, Signature>();
	}

	public void addSig(Signature sig) {
		this.sigs.put(sig.getClassName(), sig);
	}
	
	public void addSigDeclaration(Class c) {
		Signature sig = new Signature();
		sig.setClassName(c.getName());
		sig.setClassSimpleName(c.getSimpleName());
		sig.setClass(c);
		try {
			sig.setSig(new PrimSig(sig.getLabel()));
		} catch (Err e) {
			// this error is not a stop breaker
			logger.error(e);
		}
		addSig(sig);
	}
	
	public void setSigs(HashMap<String, Signature> sigs) {
		this.sigs = sigs;
	}

	public HashMap<String, Signature> getSigs() {
		return sigs;
	}
	/**
	 * Returns the Signature corresponding the Java Class Name.
	 * @param key
	 * @return
	 */
	public Signature getSig(String key)
	{
		Signature sig = sigs.get(key);
		if (sig!=null)
			return sig;
		sig = primitiveSigs.get(key);
		return sig;
	}
	/**
	 * Adds a primitive sig to the set of primitive sigs. These sigs are usually those found in models/util/
	 * in alloy bundle jar.
	 * For String class, a new sig String is created, for Boolean we use util/boolean.
	 * 
	 * currently only String and Boolean classes are supported, other classes return null.
	 * @param c
	 * @return
	 */
	public Signature addPrimitiveSig(Class c)
	{
		//check whether the class  sig is already defined
		Signature newSig = sigs.get(c.getName());
		if (newSig==null)
			newSig = primitiveSigs.get(c.getName());
		//if primitive sig is not defined then add a new sig to the set of primitive sigs and return the new sig
		if (newSig == null)
		{
			try {
				newSig = new Signature();
				
				if (String.class == c)
				{
					newSig.setClassName(c.getName());
					newSig.setClassSimpleName(c.getSimpleName());
					newSig.setSig(new PrimSig(newSig.getLabel()));
					primitiveSigs.put(c.getName(), newSig);
				}
				else if (c==boolean.class || Boolean.class ==c)
				{
					newSig.setClassName(c.getName());
					//newSig.setAlloyUtilPath("util/boolean");
					newSig.setLabel("Bool");
					newSig.setClassSimpleName(c.getSimpleName());
					newSig.setSig(new PrimSig(newSig.getLabel()));
					primitiveSigs.put(c.getName(), newSig);
				}
			} catch (Exception e) {
				logger.error("Could not create "+ c.getName() +  " signature.");
			}
		}
		return newSig;
	}
	/**
	 * Returns the Sig corresponding to the class C.
	 * C should be a primitive type, such as int, String, boolean.
	 * If the sig for C has not been created yet then a new Sig representing the primitive sig
	 * is added to model then returned.
	 * 
	 * @param c
	 * @return
	 */
	public Sig getAlloyPrimitiveSig(Class c)
	{
		if (c==int.class || Integer.class==c)
			return Sig.SIGINT;
		if (c==boolean.class || Boolean.class ==c)
		{
			Signature sig = addPrimitiveSig(c);
			if (sig==null)
				return null;
			else 
				return sig.getSig();
		}
		if (String.class == c)
		{
			Signature sig = addPrimitiveSig(c);
			if (sig==null)
				return null;
			else 
				return sig.getSig();
		}
		return null;
	}
	/**
	 * Returns all signatures in the model which where created from Java classes.
	 * It doesn't return primitive sigs such as String, boolean, int...
	 * 
	 * @param sig
	 * @return
	 */
	public Signature getSignatureFromAlloySig(Sig sig)
	{
		for(Signature s: sigs.values())
		{
			if (("this/" + s.getLabel()).equalsIgnoreCase(sig.label))
				return s;
		}
		for(Signature s: primitiveSigs.values())
		{
			if (("this/" + s.getLabel()).equalsIgnoreCase(sig.label))
				return s;
		}
		return null;
	}
	
	/**
	 * @param moduleName the moduleName to set
	 */
	public void setModuleName(String moduleName) {
		moduleName = moduleName.replace('.', '/');
		this.moduleName = moduleName;
	}
	/**
	 * @return the moduleName
	 */
	public String getModuleName() {
		return moduleName;
	}
	/**
	 * @param dependencies the dependencies to set
	 */
	public void addDependency(String dependency) {
		if (dependency!=null){
			// do not open the same module in itself
			dependency = dependency.replace('.', '/');
			if (!moduleName.equalsIgnoreCase(dependency))
				this.dependencies.add(dependency);
			
		}
	}

	/**
	 * @param dependencies the dependencies to set
	 */
	public void addDependency(Class dependencyClass) {
		if (dependencyClass!=null){
			if(dependencyClass.isPrimitive()){
				addPrimitiveSig(dependencyClass);
			}else{
				addDependency(dependencyClass.getName());
			}
		}
	}
	
	/**
	 * @param predicates to set
	 */
	public void addPredicate(Predicate pred) {
		if (pred!=null){
			// do not open the same module in itself
			this.predicates.add(pred);
		}
	}
	
	/**
	 * @return the dependencies
	 */
	public HashSet<String> getDependencies() {
		return dependencies;
	}
	
	/**
	 * @return the predicates
	 */
	public HashSet<Predicate> getPredicates() {
		return predicates;
	}
	
	public void addFact(String fact){
		facts.add(fact);
	}
	public void addFacts(Collection<String> fact){
		facts.addAll(fact);
	}
	public List<String> getFacts(){
		return facts;
	}
	
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
	
	public List<String> getBodyStmts() {
		return bodyStmts;
	}

	public void addBodyStmts(Collection<String> bodyStmts) {
		this.bodyStmts.addAll(bodyStmts);
	}
	public void addBodyStmt(String bodyStmt) {
		this.bodyStmts.add(bodyStmt);
	}
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("module " + moduleName +"\n");
		
		if (primitiveSigs.containsKey("boolean") || primitiveSigs.containsKey("Boolean")) 
			sb.append("open util/boolean as util_boolean\n");
		for(String dep : dependencies) {
			sb.append("open " + dep + " as "+dep.replace('.', '_').replace('/', '_')+"\n");
		}
		sb.append("\n-- Model signatures\n");
		for (Signature s: sigs.values()) {
			sb.append(s);
		}
		sb.append("\n-- Additional body statements\n");
		for(String stmt: bodyStmts) {
			sb.append(stmt + "\n");
		}
		
		sb.append("\n-- Model facts\n");
		if (facts.size()>0) {
			sb.append("fact {\n");
			for (String fact: facts){
				sb.append("\t" + fact+"\n");
			}
			sb.append("}\n");
		}

		sb.append("\n-- Model Predicates\n");
		for(Predicate pred : predicates) {
			sb.append(pred.toString() + "\n");
		}
		
		sb.append(command);
		
		return sb.toString();
	}
}
