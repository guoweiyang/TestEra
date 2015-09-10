package edu.utexas.testera.annotations;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
public @interface TestEra 
{
	/**
	 * The run command scope for variables.
	 * Ex: "1 List, 2 Node, 3 seq"
	 */
	String runCommand() default "";
	/**
	 * Class invariants, these are translated into Alloy facts for the class.
	 */
	String[] invariants() default {};
	/**
	 * Sets whether a class or field is used in the translation process between Java and Alloy.
	 * Default is true;
	 */
	boolean isEnabled() default true;
	/**
	 * The pre conditions of a method under test
	 */
	String[] preConditions() default {};
	/**
	 * The post conditions of a method under test
	 */	
	String[] postConditions() default {};
}
