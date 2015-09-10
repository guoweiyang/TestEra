package edu.utexas.testera.visualizer;

import java.io.File;

import org.apache.log4j.Logger;

import edu.mit.csail.sdg.alloy4.Computer;
import edu.mit.csail.sdg.alloy4.Err;
import edu.mit.csail.sdg.alloy4compiler.translator.A4Solution;
import edu.mit.csail.sdg.alloy4viz.VizGUI;

public class InstanceVisualizer {
	static Logger logger = Logger.getLogger(InstanceVisualizer.class.getName());
	
	A4Solution ans;
	int i = 0;
	VizGUI viz;
	String tempFolderName = "temp";
	
	public void visualize(A4Solution sol) {
		logger.debug("Opening a visualizer for an alloy instance");
		ans = sol;
		// test must have passed since we got solutions back
		createTempFolder();
		loadSolution();
	}
	
	private void createTempFolder() {
		File f = new File(tempFolderName);
		f.mkdir();
	}
	
	private void loadSolution(){
		String xmlPath = tempFolderName + "/solution_" + i++ + "_.xml";
		try {
			if (ans == null || !ans.satisfiable())
				return;
			logger.debug("Creating xml file @: " + xmlPath);
			ans.writeXML(xmlPath);
			if (viz == null)
				viz = new VizGUI(true, xmlPath, null, enumerator, null);
			else 
				viz.loadXML(xmlPath, true);
		} catch (Err e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private final Computer enumerator = new Computer() {
        public String compute(Object input) {
            final String arg = (String)input;
            try {
            	logger.debug("Calling visualizer on next alloy solution");
				ans = ans.next();
				loadSolution();
			} catch (Err e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}            
            return arg;
        }
    };
    
    public static void main(String args[]) {
		//new addNodeTest().test4();
	}
}
