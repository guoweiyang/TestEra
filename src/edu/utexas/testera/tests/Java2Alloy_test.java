package edu.utexas.testera.tests;

import java.io.File;
import java.io.FileWriter;
import java.util.Set;

import org.apache.log4j.PropertyConfigurator;

import edu.mit.csail.sdg.alloy4.A4Reporter;
import edu.mit.csail.sdg.alloy4.ErrorWarning;
import edu.mit.csail.sdg.alloy4compiler.ast.Command;
import edu.mit.csail.sdg.alloy4compiler.parser.CompModule;
import edu.mit.csail.sdg.alloy4compiler.parser.CompUtil;
import edu.mit.csail.sdg.alloy4compiler.translator.A4Options;
import edu.mit.csail.sdg.alloy4compiler.translator.A4Solution;
import edu.mit.csail.sdg.alloy4compiler.translator.TranslateAlloyToKodkod;
import edu.utexas.testera.alloyComponents.AlloyModel;
import edu.utexas.testera.translator.JavaToAlloy;


public class Java2Alloy_test
{
	
	public static void main(String[] args) throws Exception
	{
		PropertyConfigurator.configure("log4j.properties");
	    
		JavaToAlloy j2a = new JavaToAlloy();
		j2a.setIncludeStates(true);
		Set<AlloyModel> models = j2a.translateClasses2Sigs(List.class, List.class.getDeclaredMethod("removeBoolean", boolean.class));
		
		for(AlloyModel m : models){
			System.out.println(m.toString());
			System.out.println("=========================================\n");
		}
		
		System.exit(1);
		
		String file = null;//j2a.generateAlloyFile();
		System.out.println(file);
		
		File outPutFile = java.io.File.createTempFile("TESTERA_AUTO_ALS", ".als");
		FileWriter fw = new FileWriter(outPutFile);
		fw.write(file, 0, file.length());
		fw.close();
		
		String fileName = outPutFile.getAbsolutePath();
		System.out.println(fileName);
		
		// Alloy4 sends diagnostic messages and progress reports to the A4Reporter.
        // By default, the A4Reporter ignores all these events (but you can extend the A4Reporter to display the event for the user)
        A4Reporter rep = new A4Reporter() {
            // For example, here we choose to display each "warning" by printing it to System.out
            @Override public void warning(ErrorWarning msg) {
                System.out.print("Relevance Warning:\n"+(msg.toString().trim())+"\n\n");
                System.out.flush();
            }
        };
        // Choose some default options for how you want to execute the commands
        A4Options options = new A4Options();
        options.solver = A4Options.SatSolver.SAT4J;
		CompModule world = CompUtil.parseEverything_fromFile(rep, null, fileName);
		for (Command command: world.getAllCommands())
		{
			int i =1;
			A4Solution ans = TranslateAlloyToKodkod.execute_command(rep, world.getAllReachableSigs(), command, options);
			while (ans!=null && ans.satisfiable())
			{
				System.out.println("" + (i++));
				//Alloy2JavaGenerator a2j = new Alloy2JavaGenerator(j2a);
				//a2j.generateDataStructures(ans);
				ans = ans.next();
			}
		}
	}
}
