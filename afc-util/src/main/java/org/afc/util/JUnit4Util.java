package org.afc.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Calendar;

public class JUnit4Util {

	public static StackTraceElement getCurrentTestMethod(Class<?> testClass) {
		StackTraceElement[] trace = Thread.currentThread().getStackTrace();
		for (int i = 0; i < trace.length; i++) {
			if (trace[i].toString().startsWith(testClass.getName())) {
				return trace[i];
			}
		}
		return null;
	}

	
	public static String getCurrentTestMethodFullName(Class<?> testClass) {
		StackTraceElement element = getCurrentTestMethod(testClass);
		if (element != null) {
			return element.toString();
		}
		return "Cannot found method for " + testClass.getName();
	}

	public static String getCurrentTestMethodShortName(Class<?> testClass) {
		StackTraceElement element = getCurrentTestMethod(testClass);
		if (element != null) {
			return element.getMethodName();
		}
		return "Cannot found method for " + testClass.getName();
	}

	public static void startCurrentTest(Class<?> testClass) {
		System.out.println("START : ========== " + getCurrentTestMethodShortName(testClass) + " ==========");
		System.out.println(getCurrentTestMethodFullName(testClass));
		System.out.println();
	}

	public static void describeCurrentTest(String description) {
		System.out.println("DESC : " + description);
		System.out.println("--------------------------------------------------");
		System.out.println();
	}
	
	public static void endCurrentTest(Class<?> testClass) {
		System.out.println();
		System.out.println("END   : ========== " + getCurrentTestMethodShortName(testClass) + " ==========");
		System.out.println();
		System.out.println();
	}

	public static <T> T actual(T actual) {
		System.out.println("--------------------------------------------------");
		System.out.println("ACTUAL : [" + actual + ']');
		System.out.println("--------------------------------------------------");
		return actual;
	}

	public static <T> T expect(T expect) {
		System.out.println("--------------------------------------------------");
		System.out.println("EXPECT : [" + expect + ']');
		System.out.println("--------------------------------------------------");
		return expect;
	}

	public static void sleep(long s) {
		try {
	        Thread.sleep(s);
        } catch (InterruptedException e) {
	        e.printStackTrace();
        }
	}
	
	public static Object getPrivateMember(Object obj, String name) {
		try {
	        Field field = getField(obj.getClass(), name);
	        field.setAccessible(true);
	        return field.get(obj);
        } catch (Exception e) {
	        e.printStackTrace();
	        return null;
        }
	}

	private static Field getField(Class<?> clazz, String name) throws NoSuchFieldException {
		try {
			System.out.println("checking " + clazz.getName() + " for " + name);
	        return clazz.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
        	if (clazz.getSuperclass() != null) {
        		return getField(clazz.getSuperclass(), name);
        	} else {
        		throw e;
        	}
        }
	}
		
	
	public static Object invokePrivateMethod(Object obj, String name, Class<?>[] types, Object[] args) {
		try {
	        Method method = getMethod(obj.getClass(), name, types);
	        method.setAccessible(true);
	        return method.invoke(obj, args);
        } catch (Exception e) {
	        e.printStackTrace();
	        return null;
        }
	}

	private static Method getMethod(Class<?> clazz, String name, Class<?>[] types) throws NoSuchMethodException {
		try {
			System.out.println("checking " + clazz.getName() + " for " + name);
	        return clazz.getDeclaredMethod(name, types);
        } catch (NoSuchMethodException e) {
        	if (clazz.getSuperclass() != null) {
        		return getMethod(clazz.getSuperclass(), name, types);
        	} else {
        		throw e;
        	}
        }
	}
	
	public static String readFileAsString(String filename) {
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(filename));
			StringBuilder builder = new StringBuilder();
			char[] buffer = new char[1024];
			int length = 0;
			while ((length = reader.read(buffer)) > 0) {
				builder.append(buffer, 0, length);
			}
			return builder.toString();
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}
	
	public static void createFile(String filepath) {
		File file = new File(filepath);
		try {
	        file.createNewFile();
        } catch (IOException e1) {
	        e1.printStackTrace();
        }
	}

	public static void copyFile(File src, File dst) {
		try {
		    InputStream in = new FileInputStream(src);
		    OutputStream out = new FileOutputStream(dst);
	
		    byte[] buf = new byte[4096];
		    int len;
		    while ((len = in.read(buf)) > 0) {
		        out.write(buf, 0, len);
		    }
		    in.close();
		    out.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

	public static void copyFile(String src, String dst) {
	    copyFile(new File(src), new File(dst));
	}

	public static void copyFileWithLastModified(String src, String dst, int day) {
		File s = new File(src);
		File d = new File(dst);
	    copyFile(s, d);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, day);
	    d.setLastModified(cal.getTimeInMillis());
	}

	public static boolean deleteFile(String path) {
		File file = new File(path);
		return file.delete();
	}
	
	public static int cleanFolder(String path) {
		File directory = new File(path);
		int count = 0;
		for (File file:directory.listFiles()) {
        	System.out.println("removed " + file.getName() + " ? " + file.delete());
        	count++;
		}
		return count;
	}
}