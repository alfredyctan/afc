package org.afc.util;

import static org.afc.util.StringUtil.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClasspathUtil {

	private static final Logger logger = LoggerFactory.getLogger(ClasspathUtil.class);

	private static final char FILE_SEPARATOR = File.separatorChar;

	private static final String PATH_SEPARATOR = System.getProperty("path.separator");

	private static final String JAR = ".jar";

	private static final String CLASS = ".class";
	
	private static final Map<String, List<Class<?>>> packageClasses = new ConcurrentHashMap<>();
	
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

	public static final List<Class<?>> findClasses(String packageName) {
		return findClasses(packageName, true, false);
	}

	public static final List<Class<?>> findClasses(String packageName, boolean subPackage, boolean innerClass) {
		List<Class<?>> classes = findInRegistry(packageName);
		
		if (classes.size() == 0) {
			long start = ClockUtil.currentTimeMillis();
			String[] classPaths = System.getProperty("java.class.path").split(PATH_SEPARATOR);
			
			for (String classpath : classPaths) {
				if (classpath.endsWith(JAR)) {
					scanClassesInJar(packageName, classpath);
				} else {
					scanClassesInDir(packageName, classpath); 
				}
			}
			classes = findInRegistry(packageName);
			long elapse = ClockUtil.currentTimeMillis() - start;
			logger.info("package scan of {} took [{}]ms", packageName, elapse);
		}

		return classes.stream().filter(c -> {
			String classPackageName = c.getPackage().getName();
			if (!innerClass && c.getName().contains("$")) {
				return false;
			}
			return (subPackage) ? classPackageName.startsWith(packageName) : classPackageName.equals(packageName); 
		}).collect(Collectors.toList());
	}

	private static void register(Class<?> clazz) {
		String packageName = clazz.getPackage().getName();
		List<Class<?>> clazzes = packageClasses.get(packageName);
		if (clazzes == null) {
			clazzes = new LinkedList<>();
			packageClasses.put(packageName, clazzes);
		}
		clazzes.add(clazz);
	}
	
	private static List<Class<?>> findInRegistry(String packageName) {
		return packageClasses.entrySet().stream()
			.filter(e -> e.getKey().startsWith(packageName))
			.flatMap(e -> e.getValue().stream())
			.collect(Collectors.toList());
	}


	private static void scanClassesInJar(String packageName, String classpath) {
		File jarFile = new File(classpath);
		packageName.replaceAll("\\.", "/"); // jar show class path
		try (JarInputStream is = new JarInputStream(new FileInputStream(jarFile))) {
			for (JarEntry entry = is.getNextJarEntry(); entry != null; entry = is.getNextJarEntry()) {
				String fullClassName = entry.getName().replaceAll("/", "\\.");
				if (fullClassName.endsWith(CLASS) && fullClassName.startsWith(packageName)) {
					register(Class.forName(rightCut(fullClassName, 6)));
				}
			}
		} catch (Throwable t) {
		}
	}
	
	private static void scanClassesInDir(String packageName, String classpath) {
		String packagePath = packageName.replaceAll("\\.", "\\" + FILE_SEPARATOR);
		try {
			File packageDir = new File(classpath + FILE_SEPARATOR + packagePath);
			for (File file : packageDir.listFiles()) {
				if (file.isDirectory()) {
					scanClassesInDir(packagePath + FILE_SEPARATOR + file.getName(), classpath);
				} else {
					String classFilename = file.getAbsolutePath();
					if (classFilename.endsWith(CLASS)) {
						register(Class.forName(edgeCut(classFilename, classpath.length() + 1, 6).replaceAll("[\\\\|/]", ".")));
					}
				}
			}
		} catch (Throwable t) {
		}
	}
}
