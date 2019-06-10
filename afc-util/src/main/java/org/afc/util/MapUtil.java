package org.afc.util;

import java.util.Map;

public class MapUtil {

	public static <K, V> Map<K, V> map(Map<K, V> map, Object... keyValue) {
		if (keyValue.length % 2 != 0) {
			throw new IllegalArgumentException("missing one key or value");
		}
		for (int i = 0; i < keyValue.length; i += 2) {
			map.put((K) keyValue[i], (V) keyValue[i + 1]);
		}
		return map;
	}
}
