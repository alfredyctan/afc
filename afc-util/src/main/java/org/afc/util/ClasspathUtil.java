package org.afc.util;
import static org.afc.util.StringUtil.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.afc.ClasspathException;

public class ClasspathUtil {

	private static final Logger logger = LoggerFactory.getLogger(ClasspathUtil.class);

	private static final char FILE_SEPARATOR = File.separatorChar;

	private static final String PATH_SEPARATOR = System.getProperty("path.separator");

	private static final String JAR = ".jar";

	private static final String CLASS = ".class";

	private static final Map<String, List<Class<?>>> packageClasses = new ConcurrentHashMap<>();

	private ClasspathUtil() {}

	@SuppressWarnings({"squid:S3878", "squid:S3011"})
	public static void addSystemClasspath8(String path) {
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

	@SuppressWarnings({"squid:S3878", "squid:S3011"})
	public static void addSystemClasspath(String path) {
		ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
		if (systemClassLoader instanceof URLClassLoader) {
			try {
				Method method = systemClassLoader.getClass().getDeclaredMethod("addURL", new Class[] { URL.class });
				method.setAccessible(true);
				URL url = new File(path).toURI().toURL();
				method.invoke(systemClassLoader, new Object[] { url });
			} catch (IOException | ReflectiveOperationException e) {
				throw new ClasspathException("failed to add classpath", e);
			}
		} else {
			try {
				//--add-opens java.base/jdk.internal.loader=ALL-UNNAMED
				Method method = systemClassLoader.getClass().getDeclaredMethod("appendToClassPathForInstrumentation", new Class[] { String.class });
				method.setAccessible(true);
				method.invoke(systemClassLoader, new Object[] { path });
			} catch (ReflectiveOperationException e) {
				throw new ClasspathException("failed to add classpath", e);
			}
		}
	}

	public static final List<Class<?>> findClasses(String packageName) {
		return findClasses(packageName, true, false);
	}

	public static final List<Class<?>> findClasses(String packageName, boolean subPackage, boolean innerClass) {
		List<Class<?>> classes = findInRegistry(packageName);

		if (classes.isEmpty()) {
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
		}).distinct().collect(Collectors.toList());
	}

	private static void register(Class<?> clazz) {
		String packageName = clazz.getPackage().getName();
		List<Class<?>> clazzes = packageClasses.computeIfAbsent(packageName, k -> new LinkedList<>());
		clazzes.add(clazz);
		logger.debug("register:[{}]", clazz.getName());
	}

	private static List<Class<?>> findInRegistry(String packageName) {
		return packageClasses.entrySet().stream()
			.filter(e -> e.getKey().startsWith(packageName))
			.flatMap(e -> e.getValue().stream())
			.collect(Collectors.toList());
	}


	@SuppressWarnings({"squid:S1181"})
	private static void scanClassesInJar(String packageName, String classpath) {
		File jarFile = new File(classpath);
		packageName = packageName.replaceAll("\\.", "/"); // jar show class path
		try (JarInputStream is = new JarInputStream(new FileInputStream(jarFile))) {
			scanJarStream(is, packageName);
		} catch (Throwable t) {
			// do nothing
		}
	}

	private static void scanJarStream(JarInputStream is, String packageName) throws IOException, ClassNotFoundException {
		for (JarEntry entry = is.getNextJarEntry(); entry != null; entry = is.getNextJarEntry()) {
			String jarEntryName = entry.getName();
			if (jarEntryName.endsWith(CLASS) && jarEntryName.startsWith(packageName.replace('.', '/'))) {
				register(Class.forName(rightCut(jarEntryName.replaceAll("/", "\\."), 6)));
			} else if (jarEntryName.endsWith(JAR)) {
				scanJarStream(new JarInputStream(FileUtil.resolveResourceAsStream(jarEntryName)), packageName);
			}
		}
	}

	@SuppressWarnings({"squid:S1075", "squid:S1181"})
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
			//do nothing
		}
	}
}
