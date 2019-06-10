package org.afc.util;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class OptionalUtil {

	public static <T> void ifPresentElse(Optional<T> optional, Consumer<T> ifAction, Runnable elseAction) {
		if (optional.isPresent() && ifAction != null) {
			ifAction.accept(optional.get());
		} else {
			elseAction.run();
		}
	}

	public static <T> void ifNotNullElse(T target, Consumer<T> ifAction, Runnable elseAction) {
		if (target != null && ifAction != null) {
			ifAction.accept(target);
		} else {
			elseAction.run();
		}
	}

	public static <T> void ifNotNull(T target, Consumer<T> ifAction) {
		if (target != null && ifAction != null) {
			ifAction.accept(target);
		}
	}

	public static <T, R> R iifPresentElse(Optional<T> optional, Function<T, R> ifReturn, Supplier<R> elseReturn) {
		if (optional.isPresent() && ifReturn != null) {
			return ifReturn.apply(optional.get());
		} else {
			return elseReturn.get();
		}
	}

	public static <T, R> R iifNotNullElse(T target, Function<T, R> ifReturn, Supplier<R> elseReturn) {
		if (target != null && ifReturn != null) {
			return ifReturn.apply(target);
		} else {
			return elseReturn.get();
		}
	}

	public static <T, R> R iifNotNull(T target, Function<T, R> ifReturn) {
		if (target != null && ifReturn != null) {
			return ifReturn.apply(target);
		} else {
			return null;
		}
	}

	public static void ifNotEmptyElse(String target, Consumer<String> ifAction, Runnable elseAction) {
		if (StringUtil.hasValue(target) && ifAction != null) {
			ifAction.accept(target);
		} else {
			elseAction.run();
		}
	}

	public static void ifNotEmpty(String target, Consumer<String> ifAction) {
		if (StringUtil.hasValue(target) && ifAction != null) {
			ifAction.accept(target);
		}
	}

	public static <R> R iifNotEmptyElse(String target, Function<String, R> ifReturn, Supplier<R> elseReturn) {
		if (StringUtil.hasValue(target) && ifReturn != null) {
			return ifReturn.apply(target);
		} else {
			return elseReturn.get();
		}
	}

	public static <R> R iifNotEmpty(String target, Function<String, R> ifReturn) {
		if (StringUtil.hasValue(target) && ifReturn != null) {
			return ifReturn.apply(target);
		} else {
			return null;
		}
	}

	public static <T> void forEach(List<T> list, Consumer<T> action) {
		ifNotNull(list, t -> t.stream().forEach(action));
	}
}
