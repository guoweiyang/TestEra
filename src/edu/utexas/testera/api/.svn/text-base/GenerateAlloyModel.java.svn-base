package edu.utexas.testera.api;

import java.lang.reflect.Method;
import java.util.Set;

import edu.mit.csail.sdg.alloy4compiler.translator.A4Solution;
import edu.utexas.testera.alloyComponents.AlloyModel;
import edu.utexas.testera.io.FileGenerator;
import edu.utexas.testera.io.JUnitGenerator;
import edu.utexas.testera.main.AlloyRunner;
import edu.utexas.testera.translator.AlloyTestGenerator;
import edu.utexas.testera.translator.JavaToAlloy;
import edu.utexas.testera.utils.Constants;

public class GenerateAlloyModel {

	public static void run(Class c, Method m, String parentPath,
			java.util.List<String> paras, int limit) throws Exception {
		JavaToAlloy j2a = new JavaToAlloy();
		Set<AlloyModel> models = j2a.translateClasses2Sigs(c, m);

		FileGenerator fg = new FileGenerator(Constants.alloyOut);
		Constants.parentPath = parentPath;
		fg.setFileParentPath(Constants.parentPath + Constants.alloyOut);
		fg.generateFiles(models);

	}
}
