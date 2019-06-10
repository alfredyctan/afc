package org.afc.util;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CombinationUtil {

	private static final Logger logger = LoggerFactory.getLogger(CombinationUtil.class);

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
