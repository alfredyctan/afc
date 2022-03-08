package org.afc.junit5.extension;

import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ExtensionContext.Store;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestDurationExtension implements BeforeTestExecutionCallback, AfterTestExecutionCallback {
	private static final String START_TIME = "start_time";

	@Override
	public void beforeTestExecution(ExtensionContext context) throws Exception {
		getStore(context).put(START_TIME, System.currentTimeMillis());
		log.info("#[TEST] Running unit test [{}]", context.getRequiredTestMethod().getName());
	}

	@Override
	public void afterTestExecution(ExtensionContext context) throws Exception {
		log.info("#[TEST] Unit test [{}] took {} ms.", context.getRequiredTestMethod().getName(), System.currentTimeMillis() - getStore(context).remove(START_TIME, long.class));
	}

	private Store getStore(ExtensionContext context) {
		return context.getStore(Namespace.create(getClass(), context.getRequiredTestMethod()));
	}
}
