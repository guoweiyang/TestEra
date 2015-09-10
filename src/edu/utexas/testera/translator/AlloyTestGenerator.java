package edu.utexas.testera.translator;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import edu.utexas.testera.alloyComponents.AlloyModel;
import edu.utexas.testera.alloyComponents.Parameter;
import edu.utexas.testera.alloyComponents.Predicate;
import edu.utexas.testera.alloyComponents.StateAlloyModel;
import edu.utexas.testera.annotations.TestEra;
import static edu.utexas.testera.utils.Constants.*;

public class AlloyTestGenerator {
	private List<Class> parameterTypes;
	public AlloyModel preModel;
	public AlloyModel postModel;
	
	public void generatePreTestModel(Class c, Method m, List<String> parameterNames, Set<String> fieldNames){
		Predicate preCondition = getPreCondition(c, m, parameterNames, fieldNames);
		Predicate postCondition = getPostCondition(c, m, parameterNames, fieldNames);
		preModel = new AlloyModel();
		preModel.setModuleName(c.getName() + "." +  m.getName() + preConditionTestSuffix);
		
		String runCommand = "run " + preCondition.getName();
		String runScope = getCommandAnnt(m);
		if (runScope !=null)
			runCommand += " for " + runScope;
		preModel.setCommand(runCommand);
		//fullTestFilePath = preModel.getModuleName() + ".als";
		preModel.addPredicate(preCondition);
		//preModel.addPredicate(postCondition);
	//	predName = preCondition.getName();
		parameterTypes.add(c);
		for(Class clz: parameterTypes){
			preModel.addDependency(clz);
		}
		
		postModel = new AlloyModel();
		postModel.setModuleName(c.getName() + "." +  m.getName() + postConditionSuffix);
		postModel.addPredicate(postCondition);
		parameterTypes.add(c);
		for(Class clz: parameterTypes){
			postModel.addDependency(clz);
		}

		// IMPORTATNT
		// THIS IS FOR POST STATE SIG GENERATION if it was not previously added 
		// in the STATE signature while generating prestate
		postModel.addBodyStmt(StateAlloyModel.getPostSigDeclaration());
	}
	
	//TODO: run command, add it to model "run predName for " + annotations
	/**
	 * get annotation for run command
	 */
	public String getCommandAnnt(Method m){
		// read annotations
    	TestEra t = m.getAnnotation(TestEra.class);
    	String command = null;
    	if (t!=null){      		
        	//read the preCondtions
    		command = t.runCommand();
    	}
    	return command;
	}
	
	public Predicate getPreCondition(Class c, Method m, List<String> parameterNames, Set<String> fieldNames) {
		return getPredicate(true, c, m, parameterNames,  fieldNames);
	}
	
	public Predicate getPostCondition(Class c, Method m, List<String> parameterNames, Set<String> fieldNames){
		return getPredicate(false, c, m, parameterNames,  fieldNames);
	}
	/**
	 * generate alloy predicate from user-specified precondition
	 * @param c
	 * @param m
	 * @param parameterNames
	 * @return
	 */
	public Predicate getPredicate(boolean isPre, Class c, Method m, List<String> parameterNames, Set<String> fieldNames){
		List<Parameter> parameters = new LinkedList<Parameter>();
		
		String predName = (isPre) ? m.getName()+ preConditionSuffix : m.getName() + postConditionSuffix;
		
		parameterTypes = new LinkedList<Class>();
		Predicate pred = new Predicate(predName);
		
		// read annotations
    	TestEra t = m.getAnnotation(TestEra.class);
    	if (t!=null){      		
        	//read the preCondtions
    		String[] preConditions = isPre ? t.preConditions() : t.postConditions();
    		List<String> bodyStmts = new LinkedList<String>();
    		if (preConditions != null){
    			for(String s: preConditions){			
    				bodyStmts.add(JavaToAlloy.updatePrePost(fieldNames, s, 0));
    			}
    		}
    		// add precondition to pred body
    		pred.setBodyStmts(bodyStmts);
    	}
    	
    	m.getReturnType();
		// collect parameters list

		for (Class argType : m.getParameterTypes()) {
			parameterTypes.add(argType);
		}

		if (m.getModifiers() != Modifier.STATIC){
    		parameters.add(new Parameter(c, c.getSimpleName().toLowerCase()));
    	}
		//assert(parameterTypes.size() == parameterNames.size());
		for(int i=0; i<parameterTypes.size(); i++){
			parameters.add(new Parameter(parameterTypes.get(i), parameterNames.get(i)));
		}
		if (m.getReturnType() == Void.class) {
			parameters.add(new Parameter(m.getReturnType(),"result"));
		}
		
		// set parameters list for pred
		pred.setParameters(parameters);
		
		return pred;
	}
}
