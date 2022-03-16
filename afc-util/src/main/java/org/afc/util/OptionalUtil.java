package org.afc.util;

import static java.util.Arrays.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OptionalUtil {

	private static final Logger logger = LoggerFactory.getLogger(OptionalUtil.class);

	private OptionalUtil() {}

	public static <T> void ifPresentElse(Optional<T> optional, Consumer<T> ifAction, Runnable elseAction) {
		if (optional.isPresent() && ifAction != null) {
			ifAction.accept(optional.get());
		} else {
			elseAction.run();
		}
	}

	public static <T> void ifNull(T target, Runnable nullAction) {
		if (target == null) {
			nullAction.run();
		}
	}

	public static <T> boolean isNonNullConsumed(T target, Consumer<T> consumer) {
		if (target != null) {
			consumer.accept(target);
			return true;
		}
		return false;
	}

	public static <T> void ifThen(T supply, Predicate<T> predicate, Consumer<T> consumer) {
		if (predicate.test(supply)) {
			consumer.accept(supply);
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

	public static <T, R> R iifPresentOrNull(Optional<T> optional, Function<T, R> ifReturn) {
		if (optional.isPresent() && ifReturn != null) {
			return ifReturn.apply(optional.get());
		} else {
			return null;
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
		return iifNotNullApply(target, ifReturn);
	}

	public static <T, R> R iifNotNullApply(T target, Function<T, R> ifReturn) {
		if (target != null && ifReturn != null) {
			return ifReturn.apply(target);
		} else {
			return null;
		}
	}

	public static <T> T iifNotNull(T target, Supplier<T>  defaultReturn) {
		return iifNotNullSupply(target, defaultReturn);
	}

	public static <T> T iifNotNullSupply(T target, Supplier<T>  defaultReturn) {
		return (target != null) ? target : defaultReturn.get();
	}

	public static <T> T iifNotNull(T target, T defaultReturn) {
		return iifNotNullDefault(target, defaultReturn);
	}

	public static <T> T iifNotNullDefault(T target, T defaultReturn) {
		return (target != null) ? target : defaultReturn;
	}

	@SafeVarargs
	public static <T> T findFirst(Predicate<T> predicate, Supplier<T>... suppliers) {
		if (predicate == null || suppliers == null || suppliers.length == 0) {
			return null;
		}
		return Arrays.stream(suppliers).map(Supplier::get).filter(predicate::test).findFirst().orElse(null);
	}

	public static <T, R> R iif(T target, Predicate<T> predicate, Function<T, R> ifReturn) {
		return iif(target, predicate, ifReturn, supplyNull());
	}

	public static <T, R> R iif(T target, Predicate<T> predicate, Function<T, R> ifReturn, Supplier<R> elseReturn) {
		return predicate.test(target) ? ifReturn.apply(target) : elseReturn.get();
	}

	public static <T, R> R iif(T target, Predicate<T> predicate, Function<T, R> ifReturn, Function<T, R> elseReturn) {
		return predicate.test(target) ? ifReturn.apply(target) : elseReturn.apply(target);
	}

	public static <T, R> R iifSupply(Supplier<T> supplier, Predicate<T> predicate, Function<T, R> ifReturn) {
		return iifSupply(supplier, predicate, ifReturn, supplyNull());
	}

	public static <T, R> R iifSupply(Supplier<T> supplier, Predicate<T> predicate, Function<T, R> ifReturn, Supplier<R> elseReturn) {
		try {
			T supply = supplier.get();
			return iif(supply, predicate, ifReturn, elseReturn);
		} catch (Exception e) {
			logger.debug("iif exception:[{}] ", e.getMessage());
		}
		return null;
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

	public static <T> T orNull(Optional<T> opt) {
		return opt.orElse(null);
	}

	public static <T, R> R apply(T t, Function<T, R> func) {
		return func.apply(t);
	}

	public static <T> T accept(T t, Consumer<T> func) {
		func.accept(t);
		return t;
	}

	public static <T> Consumer<T> consume(List<Consumer<T>> consumers) {
		if (consumers == null) {
			return t -> {};
		}
		return t -> consumers.forEach(consumer -> consumer.accept(t));
	}

	@SafeVarargs
	public static <T> Consumer<T> consume(Consumer<T>... consumers) {
		return consume(asList(consumers));
	}

	public static <R> R tryTo(Supplier<R> attempt, Supplier<R> caught) {
		try {
			return attempt.get();
		} catch (Exception e) {
			return caught.get();
		}
	}

	public static <T> Supplier<T> supplyNull() {
		return () -> null;
	}

	public static <T> Function<T, T> passThru() {
		return t -> t;
	}
}
