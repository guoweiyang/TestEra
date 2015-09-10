package edu.utexas.testera.translator;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.utexas.testera.alloyComponents.AlloyModel;
import edu.utexas.testera.alloyComponents.Predicate;
import edu.utexas.testera.alloyComponents.StateAlloyModel;
import edu.utexas.testera.tests.Node;
import edu.utexas.testera.utils.Support;
import static edu.utexas.testera.utils.Constants.*;

public class StateManager {
	static Logger logger = Logger.getLogger(StateManager.class.getName());
	public static final String POST_PRED_COMMAND = "TESTERA";
	public static int DEFAULT_SCOPE = 3;
	/**
	 * generates the abstraction with the State (pre/post) relation included
	 */
	private boolean prePostEnabled = true;
	private String stateName = preSigName;
	//map between java objects and their corresponding concrete alloy names
	private HashMap<String, Object> objectList = new HashMap<String, Object>();
	// a worklist to keep track of unvisited objects while generating reference code
	private LinkedList<String> worklist = new LinkedList<String>();
	private List<String> preStateStmts = new LinkedList<String>();
	private List<String> postStateStmts = new LinkedList<String>();
	//this is a set rather than a list to store one set of statements for both pre and post
	private Set<String> sigStmts = new HashSet<String>();
	private HashMap<String, Integer> scope = new HashMap<String, Integer>();
	
	private int nameCounter = 0;
	
	public void generatePreState() {
		stateName = preSigName;
		preStateStmts = generateState();
	}
	public void generatePostState() {
		stateName = postSigName;
		postStateStmts = generateState();
	}
	public List<String> generateState(){
		logger.debug("Create " + stateName + " state statements for objectlist " + objectList);
		List<String> stmts = new LinkedList<String>();
		//assign the relation values (fields of the objects), this will 
		//recursively visit references and add then to the object lists
		worklist = new LinkedList<String>();
		worklist.addAll(objectList.keySet());
		LinkedList<String> visited = new LinkedList<String>();
		while (!worklist.isEmpty()){
			String name = worklist.removeFirst();
			if (visited.contains(name))
				continue;
			visited.add(name);
			Object o = objectList.get(name);
			//if it is primitive then ignore
			if (o==null || Support.isAlloyPrimitive(o.getClass())) continue;
			logger.debug("Create statement for object with name : " + name);
			for (Field field : o.getClass().getDeclaredFields()) {
				field.setAccessible(true);
				List<String> f_stmts = getAlloyFieldStmts(o, field);
				stmts.addAll(f_stmts);
			}
		}
		// create a sig for each variable
		// this must be done after the fields assignments because the previous step might have created
		// new objects references added to the objectList
		for(String name : objectList.keySet()){
			if (Support.isAlloyPrimitive(objectList.get(name).getClass()))
				continue;
			String stmt = "one sig " + name + " extends " + 
				objectList.get(name).getClass().getSimpleName() + " {}";
			sigStmts.add(stmt);
		}
		return stmts;
	}
	
	private List<String> getAlloyFieldStmts(Object o, Field field){
		List<String> ans = new LinkedList<String>();
		try {
			String variableName = getObjectName(o);
			String alloyField = field.getName();
			Object fieldValue = field.get(o);
			boolean isPrim = field.getType().isPrimitive();
			if (fieldValue==null || !field.getType().isArray()){
				String stmt = getAlloyStatement(isPrim, fieldValue, variableName, alloyField);
				ans.add(stmt);
			}
			else {
				isPrim = field.getType().getComponentType().isPrimitive();
				int arrayLength = Array.getLength(fieldValue);
				for (int i=0; i<arrayLength; i++){
					String stmt = getAlloyStatement(isPrim, Array.get(fieldValue, i), variableName, alloyField+"["+i+"]");
					ans.add(stmt);
				}
			}
		} catch (IllegalArgumentException e) {
			logger.error("Can not get the field "+ field+ " from object:"+ o);
		} catch (IllegalAccessException e) {
			logger.error("Dont have access to "+ field + "in object:"+ o);
		}
		return ans;
	}
	private String getAlloyStatement(boolean isPrimitive, Object value, String variableName, String alloyField) {
		if (prePostEnabled)
			alloyField = "(" + alloyField + "." + stateName +")";
		if (value==null) {
			return "no " +  variableName + "." + alloyField;
		}
		if (!isPrimitive) { 
			value = getObjectName(value);
		}
		value = updateBooleanString(value.toString());
		
		return variableName + "." + alloyField + " = " + value;
	}
	private String getObjectName(Object o) {
		for(String name : objectList.keySet()){
			if (objectList.get(name) == o)
				return name;
		}
		// object not in the list so added it automatically
		String newName = addToState(o);
		worklist.addLast(newName);
		return newName;
	}
	public void addToState(String variableName, Object variableReference){
		objectList.put(variableName, variableReference);
		System.out.println("adding " + variableName + ", " +  variableReference + " to state");
		Class c = variableReference.getClass();
		if (Support.isAlloyPrimitive(c))
			return;
		String key = c.getSimpleName();
		if (scope.get(key) == null)
			scope.put(key, 1);
		else 
			scope.put(key ,scope.get(key).intValue()+ 1);
	}
	
	public String addToState(Object variableReference){
		String variableName = variableReference.getClass().getSimpleName().toLowerCase() + "_"+nameCounter;
		nameCounter++;
		if (objectList.containsKey(variableName))
			return addToState(variableReference);
		else {
			addToState(variableName, variableReference);
			//added to the objectList using the auto generated variable name
			return variableName;
		}
	}

	/**
	 * @param prePostEnabled the prePostEnabled to set
	 */
	public void setPrePostEnabled(boolean prePostEnabled) {
		this.prePostEnabled = prePostEnabled;
	}

	/**
	 * @return the prePostEnabled
	 */
	public boolean isPrePostEnabled() {
		return prePostEnabled;
	}

	public AlloyModel generatePostTestModel(String className, String methodName, String ... postPredParamValues){
		AlloyModel model = new AlloyModel();
		model.setModuleName(className + "." +  methodName + postConditionTestSuffix);
		//model.addDependency(className + "." +  methodName + preConditionTestSuffix);
		model.addDependency(className + "." +  methodName + postConditionSuffix);
		
		Predicate p = new Predicate(POST_PRED_COMMAND);
		StringBuilder stmt = new StringBuilder(methodName + postConditionSuffix + " [");
		for (int i=0; i< postPredParamValues.length; i++){
			postPredParamValues[i] = updateBooleanString(postPredParamValues[i]);
				stmt.append(postPredParamValues[i]);
				if (i != postPredParamValues.length -1)
					stmt.append(", ");
		}
		stmt.append("]");
		p.addBodyStmt(stmt.toString());
		String runCommand = generateRunCommand();
		System.out.println(preStateStmts);
		model.addFacts(preStateStmts);
		model.addFacts(postStateStmts);
		
		model.addBodyStmts(sigStmts);
		
		model.addPredicate(p);
		model.setCommand(runCommand);
		return model;
	}
	
	private String updateBooleanString(String booleanString){
		if (booleanString.equals("true")) return "True";
		if (booleanString.equals("false")) return "False";
		return booleanString;
	}
	public String generateRunCommand(){
		StringBuilder stmt = new StringBuilder("run " + POST_PRED_COMMAND);
		if (scope.size()!=0) {
			stmt.append(" for "+ DEFAULT_SCOPE + " but exactly ");
			for (String key : scope.keySet()){
				int value = scope.get(key);
				stmt.append(value + " " + key);
				stmt.append(",");
			}
			stmt.deleteCharAt(stmt.length()-1);
		}
		return stmt.toString();
	}

	public List<String> getPreStateStmts() {
		return preStateStmts;
	}
	public List<String> getPostStateStmts() {
		return postStateStmts;
	}
	
	public Set<String> getSignatureStmts(){
		return sigStmts;
	}
}
