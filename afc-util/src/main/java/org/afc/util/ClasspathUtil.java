package org.afc.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClasspathUtil {

	private static final Logger logger = LoggerFactory.getLogger(ClasspathUtil.class); 
	
	public static void addSystemClasspath(String path) {
		try {
			URL url = new File(path).toURI().toURL();
			URLClassLoader urlClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
			Class<URLClassLoader> urlClass = URLClassLoader.class;
			Method method = urlClass.getDeclaredMethod("addURL", new Class[] { URL.class });
			method.setAccessible(true);
			method.invoke(urlClassLoader, new Object[] { url });
		} catch (IOException | ReflectiveOperationException e) {
			throw new RuntimeException("failed to add classpath", e);
		}
	}
}
