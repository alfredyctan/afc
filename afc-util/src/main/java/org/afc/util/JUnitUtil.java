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
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;

import org.junit.jupiter.api.TestInfo;
import org.junit.rules.TestName;

import org.afc.jackson.JacksonUtil;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

public class JUnitUtil {

	private static final ClassPool pool = ClassPool.getDefault();
	
	private static Function<Object, String> showFormatter = null;
	
	public static void setShowFormatter(Function<Object, String> showFormatter) {
		JUnitUtil.showFormatter = showFormatter; 
	}

	public static void showJson() {
		JUnitUtil.showFormatter = JacksonUtil::toJsonString; 
	}

	public static void showAutoString() {
		JUnitUtil.showFormatter = AutoString::of; 
	}

	public static void showToString() {
		JUnitUtil.showFormatter = null; 
	}
	
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

	public static String getCurrentTestMethodFullName(Class<?> testClass, TestName name) {
		try {
			CtClass clazz = pool.get(testClass.getName());
			CtMethod method = clazz.getDeclaredMethod(name.getMethodName());
			String fileName = '(' + clazz.getClassFile().getSourceFile() + ':' + method.getMethodInfo().getLineNumber(0) + ')'; 
			return testClass.getName() + '.' + name.getMethodName() + fileName;
		} catch (Exception e) {
			return "Cannot found method for " + testClass.getName();
		}
	}

	public static String getCurrentTestMethodFullName(Class<?> testClass, TestInfo testInfo) {
		try {
			CtClass clazz = pool.get(testClass.getName());
			CtMethod method = clazz.getDeclaredMethod(testInfo.getTestMethod().get().getName());
			String fileName = '(' + clazz.getClassFile().getSourceFile() + ':' + method.getMethodInfo().getLineNumber(0) + ')'; 
			return testClass.getName() + '.' + testInfo.getTestMethod().get().getName() + fileName;
		} catch (Exception e) {
			return "Cannot found method for " + testClass.getName();
		}
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

	public static void startCurrentTest(Class<?> testClass, TestName name) {
		System.out.println("START : ========== " + name.getMethodName() + " ==========");
		System.out.println(getCurrentTestMethodFullName(testClass, name));
		System.out.println();
	}

	public static void startCurrentTestInfo(Class<?> testClass, TestInfo testInfo) {
		System.out.println("START : ========== " + testInfo.getDisplayName() + " ==========");
		System.out.println(getCurrentTestMethodFullName(testClass, testInfo));
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

	public static void endCurrentTest(Class<?> testClass, TestName name) {
		System.out.println();
		System.out.println("END   : ========== " + name.getMethodName() + " ==========");
		System.out.println();
		System.out.println();
	}

	public static void endCurrentTestInfo(Class<?> testClass, TestInfo testInfo) {
		System.out.println();
		System.out.println("END   : ========== " + testInfo.getDisplayName() + " ==========");
		System.out.println();
		System.out.println();
	}	

	public static void milestone(String milestone) {
		System.out.println("##################################################");
		System.out.println("MILESTONE : " + milestone);
		System.out.println("##################################################");
		System.out.println();
	}

	public static <T> T actual(Consumer<T> before, T actual, Consumer<T> after) {
		before.accept(actual);
		actual(actual);
		after.accept(actual);
		return actual;
	}
	
	public static <T> T actual(Consumer<T> before, T actual) {
		before.accept(actual);
		return actual(actual);
	}

	public static <T> T actual(T actual, Consumer<T> after) {
		actual(actual);
		after.accept(actual);
		return actual;
	}
	
	public static <T> T actual(T actual) {
		return show("ACTUAL", actual);
	}

	public static <T> T expect(Consumer<T> before, T expect, Consumer<T> after) {
		before.accept(expect);
		expect(expect);
		after.accept(expect);
		return expect;
	}

	public static <T> T expect(Consumer<T> before, T expect) {
		before.accept(expect);
		return expect(expect);
	}

	public static <T> T expect(T expect, Consumer<T> after) {
		expect(expect);
		after.accept(expect);
		return expect;
	}
	
	public static <T> T expect(T expect) {
		return show("EXPECT", expect);
	}

	public static <T> T describe(Consumer<T> before, T describe, Consumer<T> after) {
		before.accept(describe);
		describe(describe);
		after.accept(describe);
		return describe;
	}

	public static <T> T describe(Consumer<T> before, T describe) {
		before.accept(describe);
		return describe(describe);
	}

	public static <T> T describe(T describe, Consumer<T> after) {
		describe(describe);
		after.accept(describe);
		return describe;
	}
	
	public static <T> T describe(T describe) {
		return show("DESCRIBE", describe);
	}
	
	public static <T> T show(String text, T show) {
		System.out.println("--------------------------------------------------");
		if (show instanceof Iterable) {
			System.out.println(text + " (" + show.getClass().getName() + ") : [");
			((Iterable<?>) show).forEach(p -> 
				System.out.println("    " + ((showFormatter != null) ? showFormatter.apply(p) : p))
			);
			System.out.println(']');
		} else if (show != null && show.getClass().isArray()) {
			System.out.println(text + " (" + show.getClass().getName() + ") : [");
			for (Object e : (Object[])show) {
				System.out.println("    " + ((showFormatter != null) ? showFormatter.apply(e) : e));
			}
			System.out.println(']');
		} else if (show instanceof Map) {
			System.out.println(text + " (" + show.getClass().getName() + ") : [");
			((Map<?, ?>) show).entrySet().stream().forEach(e -> 
				System.out.println("    " + e.getKey() + '=' + ((showFormatter != null) ? showFormatter.apply(e.getValue()) : e.getValue()))
			);
			System.out.println(']');
		} else {
			System.out.println(text + " : [" + ((showFormatter != null) ? showFormatter.apply(show) : show) + ']');
		}
		System.out.println("--------------------------------------------------");
		return show;
	}
	
	public static void sleep(long s) {
		try {
			System.out.println("SLEEP : [" + s+ "]ms");
	        Thread.sleep(s);
        } catch (InterruptedException e) {
	        e.printStackTrace();
        }
	}
	
	public static void await(CountDownLatch latch, long s) {
		try {
			latch.await(s, TimeUnit.MILLISECONDS);
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
		d.getParentFile().mkdirs();
	    copyFile(s, d);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, day);
	    d.setLastModified(cal.getTimeInMillis());
	}

	public static boolean deleteFile(String path) {
		return deleteFile(new File(path));
	}

	public static boolean deleteFile(File file) {
		if (!file.isFile()) {
			File[] subfiles = file.listFiles();
			if (subfiles != null) {
				for (File subFile : subfiles) {
					deleteFile(subFile);
				}
			}
		}
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