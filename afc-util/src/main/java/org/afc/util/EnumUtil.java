package org.afc.util;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class EnumUtil {

	public static <K, E> Map<K, E> mapper(E[] values, Function<E, K> keyMapper) {
		return Arrays.asList(values).stream().collect(Collectors.toMap(keyMapper, e -> e));
	}

	public static <K, E> E from(Map<K, E> map, K key) {
		if (key == null) {
			return null;
		}
		
		E e = map.get(key);
		if (e == null) {
			throw new IllegalArgumentException("No enum constant from " + key);
		} 
		return e;
	}
}
