package org.afc.concurrent;

import java.util.concurrent.Callable;
import java.util.function.Function;

import org.afc.logging.SDC;

public class VerboseCallable<T> implements Callable<T> {

	private String sdc;

	private Callable<T> callable;

	public VerboseCallable(Callable<T> callable) {
		this.callable = callable;
	}

	public VerboseCallable(String sdc, Callable<T> callable) {
		this.sdc = sdc;
		this.callable = callable;
	}

	@Override
	public T call() throws Exception {
		SDC.set(sdc);
		return callable.call();
	}

	public static <A, T> VerboseCallable<T> verboseCall(Function<A, T> func, A arg) {
		return new VerboseCallable<>(() -> func.apply(arg));
	}
}