package edu.utexas.testera.tests;

import java.util.Set;

import org.apache.log4j.PropertyConfigurator;

import edu.utexas.testera.alloyComponents.AlloyModel;
import edu.utexas.testera.io.FileGenerator;
import edu.utexas.testera.translator.JavaToAlloy;
import edu.utexas.testera.utils.Constants;

public class GenerateFile {
	public static void main(String[] args) throws Exception
	{
		PropertyConfigurator.configure("log4j.properties");
	    
		JavaToAlloy j2a = new JavaToAlloy();
		j2a.setIncludeStates(true);
		Set<AlloyModel> models = j2a.translateClasses2Sigs(List.class, List.class.getDeclaredMethod("removeBoolean", boolean.class));
		
		FileGenerator fg = new FileGenerator(Constants.alloyOut);
		
		for(AlloyModel m : models){
			//System.out.println(m.toString());
			System.out.println("=========================================\n");
			fg.generateFile(m);
		}
	}
}
