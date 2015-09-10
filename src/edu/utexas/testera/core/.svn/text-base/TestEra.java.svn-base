package edu.utexas.testera.core;

import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;

import edu.mit.csail.sdg.alloy4compiler.translator.A4Solution;
import edu.mit.csail.sdg.alloy4viz.VizGUI;
import edu.utexas.testera.alloyComponents.AlloyModel;
import edu.utexas.testera.io.FileGenerator;
import edu.utexas.testera.main.AlloyRunner;
import edu.utexas.testera.translator.StateManager;
import edu.utexas.testera.utils.Constants;
import edu.utexas.testera.visualizer.InstanceVisualizer;

import static org.junit.Assert.*;

public class TestEra {
	static Logger logger = Logger.getLogger(TestEra.class.getName());
	
	private static boolean visualized = false;
	
	public static boolean checkPostState(StateManager sm, String className,
			String methodName, String... parameterValues){
		return checkPostState(TestEra.visualized, sm, className, methodName, parameterValues);
	}
	
	public static boolean checkPostState(boolean useVisualizer, StateManager sm, String className,
			String methodName, String... parameterValues){
		sm.generatePostState();
		AlloyModel checkPostModel = sm.generatePostTestModel(className,
				methodName, parameterValues);
		FileGenerator fg = new FileGenerator(Constants.alloyOut);
		fg.setFileParentPath(Constants.parentPath+Constants.alloyOut);
		logger.debug("post model generated:\n\n" + checkPostModel);
		String path = fg.generateFile(checkPostModel);
		try {
			AlloyRunner runner = new AlloyRunner();
			A4Solution ans = runner.run(path);
			// if no solution then the post condition was not satisfied, i.e.
			// test FAILED
			assertTrue("The post condition in alloy model could not be satisfied.", ans != null && ans.satisfiable());
			// open GUI visualizer for the alloy instance
			if (useVisualizer)
				new InstanceVisualizer().visualize(ans);
			return false;
		} catch (Exception e) {
			logger.error("Could not run the alloy model in : " + path);
			e.printStackTrace();
			assertTrue("The post condition in alloy model could not be run.", false);
		}
		return true;
	}

	/**
	 * If true, the TestEra checkPostState will open a UI visualizer to view 
	 * the Alloy pre and post states if available.
	 */
	public static void setVisualized(boolean visualized) {
		TestEra.visualized = visualized;
	}

	/**
	 * @return the visualized
	 */
	public static boolean isVisualized() {
		return visualized;
	}
}
