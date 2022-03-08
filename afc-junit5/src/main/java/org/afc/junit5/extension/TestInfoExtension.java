package org.afc.junit5.extension;

import static java.util.stream.Collectors.*;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Store;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;

import javassist.ClassPool;
import javassist.CtClass;

@SuppressWarnings({"squid:S1192"})
public class TestInfoExtension implements BeforeAllCallback, BeforeEachCallback, BeforeTestExecutionCallback, AfterTestExecutionCallback, AfterEachCallback, AfterAllCallback {

	private static final Logger logger = LoggerFactory.getLogger(TestInfoExtension.class);

	private static final ClassPool POOL = ClassPool.getDefault();

	private static final String KEY_TIME_EACH = "key_time_each";

	private static final String KEY_TIME_ALL = "key_time_all";

	@Override
	public void beforeAll(ExtensionContext context) throws Exception {
		setDuration(context, KEY_TIME_ALL);
		logger.info("START ALL : ========== {}, tag:{} ==========", context.getTestClass().orElseThrow().getName(), getTags(context));
		logger.info("");
	}

	@Override
	public void beforeEach(ExtensionContext context) throws Exception {
		setDuration(context, KEY_TIME_EACH);
		logger.info("START : ========== {}, tag:{} ==========", context.getDisplayName(), getTags(context));
		logger.info(getFullName(context));
		logger.info("");
	}

	@Override
	public void beforeTestExecution(ExtensionContext context) throws Exception {
		// override do nothing
	}

	@Override
	public void afterTestExecution(ExtensionContext context) throws Exception {
		// override do nothing
	}

	@Override
	public void afterEach(ExtensionContext context) throws Exception {
		if (context.getExecutionException().isPresent()) {
			logger.info("ERROR : ========== {}, tag:{} ==========", context.getDisplayName(), getTags(context));
			logger.info("", context.getExecutionException().orElseThrow());
		}
		logger.info("END   : ========== {}, tag:{}, run time:[{}] ==========", context.getDisplayName(), getTags(context), getDuration(context, KEY_TIME_EACH));
		logger.info("");
	}

	@Override
	public void afterAll(ExtensionContext context) throws Exception {
		logger.info("END   ALL : ========== {}, tag:{}, run time:[{}] ==========", context.getTestClass().orElseThrow().getName(), getTags(context), getDuration(context, KEY_TIME_ALL));
		logger.info("");
	}

	protected static String getTags(ExtensionContext context) {
		return "[" + context.getTags().stream().collect(joining(","))  + "]";
	}

	protected static void setDuration(ExtensionContext context, String key) {
		getStore(context).put(key, System.nanoTime());
	}

	protected static String getDuration(ExtensionContext context, String key) {
		long nanoseconds = (System.nanoTime() - getStore(context).remove(key, long.class));
		double seconds = nanoseconds / 1000000000.0;
		if (seconds > 1.0) {
			return String.format("%.2f", seconds) + " s";
		}
		double milliseconds = nanoseconds / 1000000.0;
		if (milliseconds > 0.01) {
			return String.format("%.3f", milliseconds) + " ms";
		}
		return nanoseconds + " ns";
	}

	protected  static Store getStore(ExtensionContext context) {
		return context.getStore(Namespace.create(context.getUniqueId()));
	}

	protected static String getFullName(ExtensionContext context) {
		String fileName;
		try {
			CtClass clazz = POOL.get(context.getTestClass().orElseThrow().getName());
			int lineNumber = clazz.getDeclaredMethod(context.getTestMethod().orElseThrow().getName()).getMethodInfo().getLineNumber(0);
			fileName = clazz.getClassFile().getSourceFile() + ":" + (lineNumber > 0 ? lineNumber - 1 : lineNumber);
		} catch (Exception e) {
			fileName = "N/A";
		}
		return context.getTestClass().orElseThrow().getName() + "." + context.getTestMethod().orElseThrow().getName() + "(" + fileName + ")";
	}

}
