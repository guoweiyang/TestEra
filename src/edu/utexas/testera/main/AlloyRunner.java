package edu.utexas.testera.main;

import java.io.File;
import java.util.HashMap;

import edu.mit.csail.sdg.alloy4.A4Reporter;
import edu.mit.csail.sdg.alloy4.ErrorWarning;
import edu.mit.csail.sdg.alloy4compiler.ast.Command;
import edu.mit.csail.sdg.alloy4compiler.parser.CompModule;
import edu.mit.csail.sdg.alloy4compiler.parser.CompUtil;
import edu.mit.csail.sdg.alloy4compiler.translator.A4Options;
import edu.mit.csail.sdg.alloy4compiler.translator.A4Solution;
import edu.mit.csail.sdg.alloy4compiler.translator.TranslateAlloyToKodkod;
import edu.utexas.testera.io.JUnitGenerator;

public class AlloyRunner {
	// Stores the mapping between the counter and A4Solutions
	public static A4Solution run(String alloyPath) throws Exception {

		HashMap<String, A4Solution> slns = new HashMap<String, A4Solution>();

		// Specify the alloy file used for testing
		File testAlloyFile = new File(alloyPath);
		String fileName = testAlloyFile.getAbsolutePath();
		System.out.println(fileName);

		// Alloy4 sends diagnostic messages and progress reports to the
		// A4Reporter.
		// By default, the A4Reporter ignores all these events (but you can
		// extend the A4Reporter to display the event for the user)
		A4Reporter rep = new A4Reporter() {
			// For example, here we choose to display each "warning" by printing
			// it to System.out
			@Override
			public void warning(ErrorWarning msg) {
				System.out.print("Relevance Warning:\n"
						+ (msg.toString().trim()) + "\n\n");
				System.out.flush();
			}
		};
		// Choose some default options for how you want to execute the commands
		A4Options options = new A4Options();
		options.solver = A4Options.SatSolver.SAT4J;
		CompModule world = CompUtil.parseEverything_fromFile(rep, null,
				fileName);
		// System.out.println("hello");
		// JUnitGenerator junitGen=new JUnitGenerator("");
		A4Solution ans = null;
		for (Command command : world.getAllCommands()) {
			int i = 1;

			ans = TranslateAlloyToKodkod.execute_command(rep,
					world.getAllReachableSigs(), command, options);
		}
		return ans;

	}

}
