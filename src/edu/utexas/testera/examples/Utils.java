package edu.utexas.testera.examples;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;


public class Utils {
	public static Class getClass(String parentPath, String className)
			throws Exception {
		// ClassLoader c = new ClassLoader(new URL[]{});
		File file = new File(parentPath);
		URI uri = file.toURI();
		URL url = uri.toURL();
		URL[] urls = new URL[] { url };

		// Create a new class loader with the directory
		ClassLoader loader = new URLClassLoader(urls, Thread.currentThread()
				.getContextClassLoader());

		Class cls = loader.loadClass(className);
		return cls;
	}
}
