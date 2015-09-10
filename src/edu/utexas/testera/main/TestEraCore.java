package edu.utexas.testera.main;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

import edu.mit.csail.sdg.alloy4.A4Reporter;
import edu.mit.csail.sdg.alloy4.Err;
import edu.mit.csail.sdg.alloy4.ErrorWarning;
import edu.mit.csail.sdg.alloy4compiler.ast.Command;
import edu.mit.csail.sdg.alloy4compiler.parser.CompModule;
import edu.mit.csail.sdg.alloy4compiler.parser.CompUtil;
import edu.mit.csail.sdg.alloy4compiler.translator.A4Options;
import edu.mit.csail.sdg.alloy4compiler.translator.A4Solution;
import edu.mit.csail.sdg.alloy4compiler.translator.TranslateAlloyToKodkod;
import edu.utexas.testera.alloyComponents.AlloyModel;
import edu.utexas.testera.io.FileGenerator;
import edu.utexas.testera.translator.AlloyTestGenerator;
import edu.utexas.testera.translator.JavaToAlloy;
import edu.utexas.testera.utils.Constants;

public class TestEraCore {
	public void RunTestEra(Class c, Method m, String fileParentPath, List<String> parameterNames) throws Err{
		JavaToAlloy j2a = new JavaToAlloy();
		j2a.setIncludeStates(true);
		Set<AlloyModel> models = j2a.translateClasses2Sigs(c, m);
		
		FileGenerator fg = new FileGenerator(Constants.alloyOut);
		fg.setFileParentPath(Constants.parentPath+Constants.alloyOut);
		fg.generateFiles(models);
		
		AlloyTestGenerator atg = new AlloyTestGenerator();
		atg.generatePreTestModel(c, m, parameterNames, j2a.getFieldNames());
		AlloyModel model = atg.preModel;
		String fileName = fg.generateFile(model);
		
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
		for (Command command : world.getAllCommands()) {
			int i = 1;
			A4Solution ans = TranslateAlloyToKodkod.execute_command(rep,
					world.getAllReachableSigs(), command, options);
			if (ans != null && ans.satisfiable()) {
				A4Solution solution = ans;
			}
		}
	}
}
