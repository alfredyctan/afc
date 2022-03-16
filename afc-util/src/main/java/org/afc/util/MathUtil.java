package org.afc.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;
import java.util.List;

import org.afc.AFCException;


@SuppressWarnings("squid:S1192")
public class MathUtil {

	public static final BigDecimal HUNDRED = new BigDecimal(100);

	/**
	 * between mask lower and upper exclusive
	 */
	public static final int OO = 0;

	/**
	 * between mask lower exclusive, upper inclusive
	 */
	public static final int OI = 1;

	/**
	 * between mask lower inclusive, upper exclusive
	 */
	public static final int IO = 2;

	/**
	 * between mask lower and upper inclusive
	 */
	public static final int II = 3;

	private MathUtil() {}

	public static BigDecimal bigDecimal(String val) {
		return (val == null) ? null : new BigDecimal(val);
	}

	public static BigDecimal bigDecimal(String val, BigDecimal defaultValue) {
		try {
			return bigDecimal(val);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public static int parseInt(String s, String failureMsg) {
		try {
			return Integer.parseInt(s);
		} catch (Exception e) {
			throw new AFCException(failureMsg + ", root cause : " + e.getMessage());
		}
	}

	public static long parseLong(String s, String failureMsg) {
		try {
			return Long.parseLong(s);
		} catch (Exception e) {
			throw new AFCException(failureMsg + ", root cause : " + e.getMessage());
		}
	}

	public static float parseFloat(String s, String failureMsg) {
		try {
			return Float.parseFloat(s);
		} catch (Exception e) {
			throw new AFCException(failureMsg + ", root cause : " + e.getMessage());
		}
	}

	public static double parseDouble(String s, String failureMsg) {
		try {
			return Double.parseDouble(s);
		} catch (Exception e) {
			throw new AFCException(failureMsg + ", root cause : " + e.getMessage());
		}
	}

	public static BigDecimal fromPercentage(BigDecimal val) {
		return (val == null) ? null : val.divide(HUNDRED);
	}

	public static BigDecimal toPercentage(BigDecimal val) {
		return (val == null) ? null : val.multiply(HUNDRED);
	}

	public static BigDecimal random(double from, double to, int scale) {
		return BigDecimal.valueOf(Math.random() * (to - from) + from).setScale(scale, RoundingMode.HALF_UP);
	}

	public static long random(long ceiling) {
		return (long)(Math.random() * ceiling);
	}

	public static long random(long floor, long ceiling) {
		return (long)(Math.random() * (ceiling - floor)) + floor;
	}

	public static <T> boolean equalTo(Comparable<T> left, T right) {
		if (left == null || right == null) {
			return left == null && right == null;
		}
		return left.compareTo(right) == 0;
	}

	public static <T> boolean greater(Comparable<T> left, T right) {
		return left.compareTo(right) > 0;
	}

	public static <T> boolean lesser(Comparable<T> left, T right) {
		return left.compareTo(right) < 0;
	}

	public static <T> boolean greaterEqual(Comparable<T> left, T right) {
		return left.compareTo(right) >= 0;
	}

	public static <T> boolean lesserEqual(Comparable<T> left, T right) {
		return left.compareTo(right) <= 0;
	}

	/**
	 * check value between range, default lower and upper bound inclusive
	 *
	 * @param <T> - any Comparable
	 * @param lower - lower bound of the range
	 * @param upper - upper bound of the range
	 * @param value - the value to check
	 * @return - true if with range
	 */
	public static <T> boolean between(Comparable<T> lower, Comparable<T> upper, T value) {
		return between(lower, upper, value, II);
	}

	/**
	 * check value between range, unknown mask lower and upper bound inclusive
	 *
	 * @param <T> - any Comparable
	 * @param lower - lower bound of the range
	 * @param upper - upper bound of the range
	 * @param value - the value to check
	 * @param inclusive - inclusion mask (OO, OI, IO, II)
	 * @return - true if with range
	 */
	public static <T> boolean between(Comparable<T> lower, Comparable<T> upper, T value, int inclusive) {
		switch (inclusive) {
			case OO:
				return lower.compareTo(value) < 0 && upper.compareTo(value) > 0;
			case OI:
				return lower.compareTo(value) < 0 && upper.compareTo(value) >= 0;
			case IO:
				return lower.compareTo(value) <= 0 && upper.compareTo(value) > 0;
			case II:
			default:
				return lower.compareTo(value) <= 0 && upper.compareTo(value) >= 0;
		}
	}

	public static int factorial(int n) {
		int fact = 1;
		for (int i = 2; i <= n; i++) {
			fact *= i;
		}
		return fact;
	}

	public static int nCr(int n, int r) {
		return factorial(n) / (factorial(r) * factorial(n - r));
	}

	public static <T> List<List<T>> generate(List<T> elements, int r) {
		List<List<T>> combinations = new LinkedList<>();
		combination(combinations, elements, r, elements.size());
		return combinations;
	}

	private static <T> void combination(List<List<T>> combinations, List<T> elements, int r, int index) {
		if (elements.size() == r) {
			combinations.add(elements);
			return;
		}

		for (int j = index - 1; j >= 0; j--) {
			List<T> subElements = new LinkedList<>(elements);
			subElements.remove(j);
			combination(combinations, subElements, r, j);
		}
	}
}