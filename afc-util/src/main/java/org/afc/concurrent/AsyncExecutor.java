package org.afc.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.afc.AFCException;
import org.afc.logging.SDC;

public final class AsyncExecutor {

	private AsyncExecutor() {}

	private static ExecutorService inline = Executors.newSingleThreadExecutor(new NamedThreadFactory("async-executor-inline"));

	private static ExecutorService concurrent = Executors.newFixedThreadPool(200, new NamedThreadFactory("async-executor-pooled"));

	public static <T> Future<T> inline(Callable<T> callable) {
		return inline.submit(new VerboseCallable<>(SDC.peek(),callable));
	}

	public static <T> Future<T> concurrent(Callable<T> callable) {
		return concurrent.submit(new VerboseCallable<>(SDC.peek(), callable));
	}

	public static <T> T get(Future<T> future) {
		try {
			return future.get();
		} catch (InterruptedException | ExecutionException e) {
			Thread.currentThread().interrupt();
			throw new AFCException(e);
		}
	}
}
