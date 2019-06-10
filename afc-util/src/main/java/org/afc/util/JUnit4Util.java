package org.afc.util;

import java.io.File;

import org.junit.rules.TestName;

import javassist.ClassPool;

public class JUnit4Util {

	private static final ClassPool pool = ClassPool.getDefault();

	public static StackTraceElement getCurrentTestMethod(Class<?> testClass) {
		return JUnitUtil.getCurrentTestMethod(testClass);
	}

	public static String getCurrentTestMethodFullName(Class<?> testClass) {
		return JUnitUtil.getCurrentTestMethodFullName(testClass);
	}

	public static String getCurrentTestMethodFullName(Class<?> testClass, TestName name) {
		return JUnitUtil.getCurrentTestMethodFullName(testClass, name);
	}

	public static String getCurrentTestMethodShortName(Class<?> testClass) {
		return JUnitUtil.getCurrentTestMethodShortName(testClass);
	}

	public static void startCurrentTest(Class<?> testClass) {
		JUnitUtil.startCurrentTest(testClass);
	}

	public static void startCurrentTest(Class<?> testClass, TestName name) {
		JUnitUtil.startCurrentTest(testClass, name);
	}

	public static void describeCurrentTest(String description) {
		JUnitUtil.describeCurrentTest(description);
	}

	public static void endCurrentTest(Class<?> testClass) {
		JUnitUtil.endCurrentTest(testClass);
	}

	public static void endCurrentTest(Class<?> testClass, TestName name) {
		JUnitUtil.endCurrentTest(testClass, name);
	}

	public static void milestone(String milestone) {
		JUnitUtil.milestone(milestone);
	}

	public static <T> T actual(T actual) {
		return JUnitUtil.actual(actual);
	}

	public static <T> T expect(T expect) {
		return JUnitUtil.expect(expect);
	}

	public static void sleep(long s) {
		JUnitUtil.sleep(s);
	}

	public static Object getPrivateMember(Object obj, String name) {
		return JUnitUtil.getPrivateMember(obj, name);
	}

	public static Object invokePrivateMethod(Object obj, String name, Class<?>[] types, Object[] args) {
		return JUnitUtil.invokePrivateMethod(obj, name, types, args);
	}

	public static String readFileAsString(String filename) {
		return JUnitUtil.readFileAsString(filename);
	}

	public static void createFile(String filepath) {
		JUnitUtil.createFile(filepath);
	}

	public static void copyFile(File src, File dst) {
		JUnitUtil.copyFile(src, dst);
	}

	public static void copyFile(String src, String dst) {
		JUnitUtil.copyFile(src, dst);
	}

	public static void copyFileWithLastModified(String src, String dst, int day) {
		JUnitUtil.copyFileWithLastModified(src, dst, day);
	}

	public static boolean deleteFile(String path) {
		return JUnitUtil.deleteFile(path);
	}

	public static int cleanFolder(String path) {
		return JUnitUtil.cleanFolder(path);
	}
}