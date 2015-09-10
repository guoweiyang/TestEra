package edu.utexas.testera.io;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.utexas.testera.alloyComponents.AlloyModel;

public class FileGenerator {
	static Logger logger = Logger.getLogger(FileGenerator.class.getName());

	private boolean replaceOriginal = true;
	private String fileParentPath = "";

	public FileGenerator() {
	}

	public FileGenerator(String parentFilePath) {
		setFileParentPath(parentFilePath);
	}

	/**
	 * @param replaceOriginal
	 *            the replaceOriginal to set
	 */

	public void setReplaceOriginal(boolean replaceOriginal) {
		this.replaceOriginal = replaceOriginal;
	}

	/**
	 * @return the replaceOriginal
	 */
	public boolean isReplaceOriginal() {
		return replaceOriginal;
	}

	/**
	 * @param fileParentPath
	 *            the fileParentPath to set
	 */
	public void setFileParentPath(String fileParentPath) {
		if (fileParentPath != null && !fileParentPath.equals("")) {
			this.fileParentPath = fileParentPath;
			if (!fileParentPath.endsWith("/"))
				this.fileParentPath += "/";
		}
	}

	/**
	 * @return the fileParentPath
	 */
	public String getFileParentPath() {
		return fileParentPath;
	}

	public void generateFiles(Set<AlloyModel> models) {
		for (AlloyModel model : models) {
			generateFile(model);
		}
	}

	public String generateFile(AlloyModel model) {
		try {
			String outputName = fileParentPath + model.getModuleName() + ".als";
			String directory = outputName.substring(0,
					outputName.lastIndexOf("/"));
			//System.out.println("alloy:" + outputName);
			logger.debug("Creating alloy directories for " + directory);
			File f = new File(directory);
			f.mkdirs();

			logger.debug("Generating alloy file " + outputName);
			FileWriter fw = new FileWriter(outputName);
			fw.write(model.toString());
			fw.close();
			logger.debug("Done alloy file generation of " + outputName);
			// TODO: outputName or fileName???
			return outputName;
		} catch (IOException e) {
			e.printStackTrace();

			// TODO Auto-generated catch block
			logger.error(e.getStackTrace());
			return null;
		}
	}

	public String generateFile(String path, String junit) {
		try {
			String outputName = fileParentPath + path + "Test.java";
			//System.out.println(outputName);
			String directory = outputName.substring(0,
					outputName.lastIndexOf("/"));

			logger.debug("Creating junit directories for " + directory);
			File f = new File(directory);
			f.mkdirs();

			logger.debug("Generating file " + outputName);
			FileWriter fw = new FileWriter(outputName);
			fw.write(junit);
			fw.close();
			logger.debug("Done junit generation of " + outputName);
			// TODO: outputName or fileName???
			return outputName;
		} catch (IOException e) {
			e.printStackTrace();

			// TODO Auto-generated catch block
			logger.error(e.getStackTrace());
			return null;
		}
	}
}
