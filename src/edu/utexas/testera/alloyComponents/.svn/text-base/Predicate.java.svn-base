package edu.utexas.testera.alloyComponents;

import java.util.LinkedList;
import java.util.List;


public class Predicate {
	private String name;
	private List<Parameter> parameters;
	private List<String> bodyStmts;
	
	
	public Predicate(String name)
	{
		this.name = name;
		bodyStmts = new LinkedList<String>();
		parameters = new LinkedList<Parameter>();
	}

	/**
	 * add a body statement
	 * @param stmt
	 */
	public void addBodyStmt(String stmt){
		if(stmt!=null && stmt.length()>0){
			bodyStmts.add(stmt);
		}
	}
	
	/**
	 * add a parameter statement
	 * @param stmt
	 */
	public void addParameter(Parameter para){
		if(para!=null){
			parameters.add(para);
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Parameter> getParameters() {
		return parameters;
	}

	public void setParameters(List<Parameter> parameters) {
		this.parameters = parameters;
	}

	public List<String> getBodyStmts() {
		return bodyStmts;
	}

	public void setBodyStmts(List<String> bodyStmts) {
		this.bodyStmts = bodyStmts;
	}
	
	public String toString(){
		StringBuffer sb = new StringBuffer();
		// pred declaration:
		sb.append("pred " + this.name);
		sb.append("(");
		for(int i=0; i<parameters.size(); i++){
			if(i==0){
				sb.append(parameters.get(i).toString());
				continue;
			}
			sb.append(", " + parameters.get(i).toString());
		}
		sb.append("){" + "\n");
		
		// pred stmts body
		for(String stmt: bodyStmts){
			sb.append("\t" + stmt + "\n");
		}
		
		// pred end
		sb.append("}" + "\n");
		
		return sb.toString();
	}

}
