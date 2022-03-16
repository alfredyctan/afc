package org.afc.util;

import static org.afc.util.DateUtil.*;
import static org.afc.util.OptionalUtil.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

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
import java.nio.file.Files;
import java.nio.file.attribute.FileTime;
import java.time.OffsetDateTime;
import java.util.Calendar;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;

import org.afc.jackson.JacksonUtil;
import org.jmock.Mockery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.function.Executable;
import org.junit.rules.TestName;
import org.mockito.invocation.InvocationOnMock;
import org.slf4j.Logger;
import org.springframework.core.env.Environment;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

public class JUnitUtil {

	private static final ClassPool pool = ClassPool.getDefault();

	private static Logger logger;

	private static Function<Object, String> showFormatter = null;

	private JUnitUtil() {}

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

	public static void setLogger(Logger logger) {
		JUnitUtil.logger = logger;
	}

	public static void resetLogger() {
		JUnitUtil.logger = null;
	}

	@SuppressWarnings("squid:S106")
	private static void println(Object s) {
		if (logger == null) {
			System.out.println(s.toString());
		} else {
			logger.info("{}", s);
		}
	}

	@SuppressWarnings("squid:S106")
	private static void println() {
		if (logger == null) {
			System.out.println();
		} else {
			logger.info("");
		}
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
		return testInfo.getTestMethod().map(testMethod -> {
			try {
				CtClass clazz = pool.get(testClass.getName());
				CtMethod method = clazz.getDeclaredMethod(testMethod.getName());
				String fileName = '(' + clazz.getClassFile().getSourceFile() + ':' + method.getMethodInfo().getLineNumber(0) + ')';
				return testClass.getName() + '.' + testMethod.getName() + fileName;
			} catch (Exception e) {
				return "Cannot found method for " + testClass.getName();
			}
		}).orElse("");
	}

	public static String getCurrentTestMethodShortName(Class<?> testClass) {
		StackTraceElement element = getCurrentTestMethod(testClass);
		if (element != null) {
			return element.getMethodName();
		}
		return "Cannot found method for " + testClass.getName();
	}

	public static void startCurrentTest(Class<?> testClass) {
		println("START : ========== " + getCurrentTestMethodShortName(testClass) + " ==========");
		println(getCurrentTestMethodFullName(testClass));
		println();
	}

	public static void startCurrentTest(Class<?> testClass, TestName name) {
		println("START : ========== " + name.getMethodName() + " ==========");
		println(getCurrentTestMethodFullName(testClass, name));
		println();
	}

	public static void startCurrentTestInfo(Class<?> testClass, TestInfo testInfo) {
		println("START : ========== " + testInfo.getDisplayName() + " ==========");
		println(getCurrentTestMethodFullName(testClass, testInfo));
		println();
	}

	public static void describeCurrentTest(String description) {
		println("DESC : " + description);
		println("--------------------------------------------------");
		println();
	}

	public static void endCurrentTest(Class<?> testClass) {
		println();
		println("END   : ========== " + getCurrentTestMethodShortName(testClass) + " ==========");
		println();
		println();
	}

	@SuppressWarnings("squid:S1172")
	public static void endCurrentTest(Class<?> testClass, TestName name) {
		println();
		println("END   : ========== " + name.getMethodName() + " ==========");
		println();
		println();
	}

	@SuppressWarnings("squid:S1172")
	public static void endCurrentTestInfo(Class<?> testClass, TestInfo testInfo) {
		println();
		println("END   : ========== " + testInfo.getDisplayName() + " ==========");
		println();
		println();
	}

	public static void milestone(String milestone) {
		println("##################################################");
		println("MILESTONE : " + milestone);
		println("##################################################");
		println();
	}

	public static <T> T actual(Consumer<T> before, T actual, Consumer<T> after) {
		ifNotNull(before, b -> b.accept(actual));
		actual(actual);
		ifNotNull(after, a -> a.accept(actual));
		return actual;
	}

	public static <T> T actual(Consumer<T> before, T actual) {
		ifNotNull(before, b -> b.accept(actual));
		return actual(actual);
	}

	public static <T> T actual(T actual, Consumer<T> after) {
		actual(actual);
		ifNotNull(after, a -> a.accept(actual));
		return actual;
	}

	public static <T> T actual(T actual) {
		return describe("ACTUAL", actual);
	}

	public static <T> T expect(Consumer<T> before, T expect, Consumer<T> after) {
		ifNotNull(before, b -> b.accept(expect));
		expect(expect);
		ifNotNull(after, a -> a.accept(expect));
		return expect;
	}

	public static <T> T expect(Consumer<T> before, T expect) {
		ifNotNull(before, b -> b.accept(expect));
		return expect(expect);
	}

	public static <T> T expect(T expect, Consumer<T> after) {
		expect(expect);
		ifNotNull(after, a -> a.accept(expect));
		return expect;
	}

	public static <T> T expect(T expect) {
		return describe("EXPECT", expect);
	}

	public static <T> T describe(Consumer<T> before, T describe, Consumer<T> after) {
		return describe(describe.getClass().getSimpleName(), before, describe, after);
	}

	public static <T> T describe(Consumer<T> before, T describe) {
		return describe(describe.getClass().getSimpleName(), before, describe);
	}

	public static <T> T describe(T describe, Consumer<T> after) {
		return describe(describe.getClass().getSimpleName(), describe, after);
	}

	public static <T> T describe(T describe) {
		return describe(describe.getClass().getSimpleName(), describe);
	}

	public static String stage(String stage) {
		println("--------------------------------------------------");
		println("STAGE : [" + stage + "]");
		println("--------------------------------------------------");
		return stage;
	}

	public static <T> T describe(String text, Consumer<T> before, T describe, Consumer<T> after) {
		ifNotNull(before, b -> b.accept(describe));
		describe(text, describe);
		ifNotNull(after, a -> a.accept(describe));
		return describe;
	}

	public static <T> T describe(String text, Consumer<T> before, T describe) {
		ifNotNull(before, b -> b.accept(describe));
		return describe(text, describe);
	}

	public static <T> T describe(String text, T describe, Consumer<T> after) {
		describe(text, describe);
		ifNotNull(after, a -> a.accept(describe));
		return describe;
	}

	@SuppressWarnings("squid:S3776")
	public static <T> T describe(String text, T describe) {
		println("--------------------------------------------------");
		if (describe instanceof Iterable) {
			println(text + " (" + describe.getClass().getName() + ") : [");
			((Iterable<?>) describe).forEach(p -> {
				println(((showFormatter != null) ? showFormatter.apply(p) : p));
				println("~~~~~~~~~~~~~~~~~~~~~~~~");
			});
			println(']');
		} else if (describe != null && describe.getClass().isArray()) {
			println(text + " (" + describe.getClass().getName() + ") : [");
			for (Object e : (Object[])describe) {
				println(((showFormatter != null) ? showFormatter.apply(e) : e));
				println("~~~~~~~~~~~~~~~~~~~~~~~~");
			}
			println(']');
		} else if (describe instanceof Map) {
			println(text + " (" + describe.getClass().getName() + ") : [");
			((Map<?, ?>) describe).entrySet().stream().forEach(e -> {
				println("" + e.getKey() + '=' + ((showFormatter != null) ? showFormatter.apply(e.getValue()) : e.getValue()));
				println("~~~~~~~~~~~~~~~~~~~~~~~~");
			});
			println(']');
		} else {
			println(text + " : [" + ((showFormatter != null) ? showFormatter.apply(describe) : describe) + ']');
		}
		println("--------------------------------------------------");
		return describe;
	}

	public static void sleep(long s, String reason) {
		try {
			println("SLEEP : [" + s+ "]ms - " + reason);
	        Thread.sleep(s);
        } catch (InterruptedException e) {
        	Thread.currentThread().interrupt();
	        e.printStackTrace();
        }
	}

	public static void sleep(long s) {
		try {
			println("SLEEP : [" + s+ "]ms");
	        Thread.sleep(s);
        } catch (InterruptedException e) {
        	Thread.currentThread().interrupt();
	        e.printStackTrace();
        }
	}

	public static void await(CountDownLatch latch, long s) {
		try {
			println("latch " + latch.hashCode() + " awaiting");
			if (!latch.await(s, TimeUnit.MILLISECONDS)) {
				println("latch " + latch.hashCode() + " timeout");
			}
        } catch (InterruptedException e) {
        	Thread.currentThread().interrupt();
	        e.printStackTrace();
        }
	}

	@SuppressWarnings("squid:S3011")
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
			println("checking " + clazz.getName() + " for " + name);
	        return clazz.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
        	if (clazz.getSuperclass() != null) {
        		return getField(clazz.getSuperclass(), name);
        	} else {
        		throw e;
        	}
        }
	}

	@SuppressWarnings("squid:S3011")
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
			println("checking " + clazz.getName() + " for " + name);
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
		try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
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

	@SuppressWarnings("squid:S899")
	public static void createFolder(String folderPath) {
		File file = new File(folderPath);
        file.mkdirs();
	}

	@SuppressWarnings("squid:S899")
	public static void createFile(String filepath) {
		File file = new File(filepath);
		try {
	        file.createNewFile();
        } catch (IOException e1) {
	        e1.printStackTrace();
        }
	}

	public static void createFile(String filepath, String creation, String lastModified, String lastAccess) {
		File file = new File(filepath);
		try {
	        file.createNewFile();
	        if (creation != null) {
	        	Files.setAttribute(file.toPath(), "creationTime", FileTime.fromMillis(offsetDateTime(creation).toInstant().toEpochMilli()));
	        }
	        if (lastModified != null) {
	        	Files.setAttribute(file.toPath(), "lastModifiedTime", FileTime.fromMillis(offsetDateTime(lastModified).toInstant().toEpochMilli()));
	        }
	        if (lastAccess != null) {
	        	Files.setAttribute(file.toPath(), "lastAccessTime", FileTime.fromMillis(offsetDateTime(lastAccess).toInstant().toEpochMilli()));
	        }
        } catch (IOException e1) {
	        e1.printStackTrace();
        }
	}

	@SuppressWarnings("squid:S899")
	public static void copyFile(File src, File dst) {
		try (
		    InputStream in = new FileInputStream(src);
		    OutputStream out = new FileOutputStream(dst);
		) {

		    byte[] buf = new byte[4096];
		    int len;
		    while ((len = in.read(buf)) > 0) {
		        out.write(buf, 0, len);
		    }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

	public static void copyFile(String src, String dst) {
	    copyFile(new File(src), new File(dst));
	}

	@SuppressWarnings("squid:S899")
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

	@SuppressWarnings("squid:S4042")
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

	@SuppressWarnings("squid:S4042")
	public static int cleanFolder(String path) {
		File directory = new File(path);
		int count = 0;
		for (File file:directory.listFiles()) {
        	println("removed " + file.getName() + " ? " + file.delete());
        	count++;
		}
		return count;
	}

	public static String getLocalApiBasePath(Environment env) {
		return "http://127.0.0.1:" + env.getProperty("local.server.port") + env.getProperty("server.servlet.context-path") + env.getProperty("api-context");
	}

	@SuppressWarnings("squid:S1172")
	public static <R> R getArg(InvocationOnMock invocation, int i, Class<R> clazz) {
		return (R)invocation.getArguments()[i];
	}

	@SuppressWarnings("squid:S1181")
	public static Callable<Boolean> isSatisfied(Mockery mockery) {
		return () -> {
			try {
				mockery.assertIsSatisfied();
				return true;
			} catch (Throwable t) {
				return false;
			}
		};
	}


	public static <V> V fail() {
		return fail(null);
	}

	public static <V> V fail(String message) {
		return Assertions.fail(message);
	}

	public static <T extends Throwable> T assertThrows(Class<T> expectedType, Executable executable) {
		return assertThrows(null, expectedType, executable);
	}

	public static <T extends Throwable> T assertThrows(String reason, Class<T> expectedType, Executable executable) {
		return Assertions.assertThrows(expectedType, executable, reason);
	}

	public static void assertTrue(boolean condition) {
		assertTrue(null, condition);
	}

	public static void assertTrue(String reason, boolean condition) {
		Assertions.assertTrue(condition, reason);
	}

	public static void assertFalse(boolean condition) {
		assertFalse(null, condition);
	}

	public static void assertFalse(String reason, boolean condition) {
		Assertions.assertFalse(condition, reason);
	}

	public static void assertNull(Object actual) {
		assertNull(null, actual);
	}

	public static void assertNull(String reason, Object actual) {
		Assertions.assertNull(actual, reason);
	}

	public static void assertNotNull(Object actual) {
		assertNotNull(null, actual);
	}

	public static void assertNotNull(String reason, Object actual) {
		Assertions.assertNotNull(actual, reason);
	}

	public static <T> void assertEquals(T actual, T expect) {
		assertEquals(null, actual, expect);
	}

	public static <T> void assertEquals(String reason, T actual, T expect) {
		assertThat(reason, actual, is(equalTo(expect)));
	}

	public static <T> void assertEquals(T[] actual, T[] expect) {
		assertEquals(null, actual, expect);
	}

	public static <T> void assertEquals(String reason, T[] actual, T[] expect) {
		assertThat(reason, actual, is(equalTo(expect)));
	}

	public static <T> void assertNotEquals(T actual, T expect) {
		assertNotEquals(null, actual, expect);
	}

	public static <T> void assertNotEquals(String reason, T actual, T expect) {
		assertThat(reason, actual, is(not(equalTo(expect))));
	}

	public static <T> void assertContains(String reason, Collection<T> actual, Collection<T> expect) {
		if (expect == null) {
			assertNull(reason, actual);
			return;
		}
		assertThat(reason, actual, containsInAnyOrder(expect.toArray()));
	}

	public static <T> void assertNotContains(String reason, Collection<T> actual, Collection<T> expect) {
		if (expect == null) {
			assertNull(reason, actual);
			return;
		}
		assertThat(reason, actual, not(containsInAnyOrder(expect.toArray())));
	}

	public static <K,V> void assertMap(String reason, Map<K, V> actual, Map<K, V> expect) {
		if (expect == null) {
			assertNull(reason, actual);
			return;
		}
		assertThat(reason, actual.entrySet(), containsInAnyOrder(expect.entrySet().toArray()));
	}
}