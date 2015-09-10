package edu.utexas.testera.alloyComponents;

import edu.utexas.testera.utils.Support;

public class Parameter {
	//TODO: support for primitive types?
	private Class type; 
	private String name;
	
	public Parameter(Class type, String name){
		this.type = type;
		this.name = name;
	}
	
	public Class getSig() {
		return type;
	}
	public void setSig(Class type) {
		this.type = type;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String toString(){
		return  this.name + ": " + Support.getAlloyName(this.type);
		//TODO: primitive - dependencies, here
	}
	
}
