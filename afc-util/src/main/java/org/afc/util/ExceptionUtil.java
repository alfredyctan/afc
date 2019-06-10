package org.afc.util;

import java.util.function.Function;
import java.util.function.Supplier;

public class ExceptionUtil {

	public static <T extends Throwable, R> R catching(Throwable t, Class<T> c, Function<T, R> f) {
		Throwable target = t;
		while (!c.isAssignableFrom(target.getClass()) && target.getCause() != null) {
			target = target.getCause();
		}
		if (c.isAssignableFrom(target.getClass())) {
			return f.apply((T) target);
		} else if (RuntimeException.class.isAssignableFrom(t.getClass())) {
			throw (RuntimeException) t;
		} else {
			throw new RuntimeException(t);
		}
	}


	public static <T extends Throwable> T unwrap(Throwable t) {
		Throwable target = t;
		while (target.getCause() != null) {
			target = target.getCause();
		}
		return (T)target;
	}

	public static <R> R tryTo(Supplier<R> attempt, Supplier<R> caught) {
		try {
			return attempt.get();
		} catch (Exception e) {
			return caught.get();
		}
	}
	
}