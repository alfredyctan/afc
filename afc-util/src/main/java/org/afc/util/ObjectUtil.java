package org.afc.util;

public final class ObjectUtil {

	public static boolean isAllNull(Object... objs) {
		for (int i = 0; i < objs.length; i++) {
			if (objs[i] != null) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean isAllNotNull(Object... objs) {
		for (int i = 0; i < objs.length; i++) {
			if (objs[i] == null) {
				return false;
			}
		}
		return true;
	}

	public static boolean isAnyoneNull(Object... objs) {
		for (int i = 0; i < objs.length; i++) {
			if (objs[i] == null) {
				return true;
			}
		}
		return false;
	}

	public static boolean isAnyoneNotNull(Object... objs) {
		for (int i = 0; i < objs.length; i++) {
			if (objs[i] != null) {
				return true;
			}
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
    public static <C> C cast(Object obj) {
		return (C)obj;
	}
	
	public static String arrayToString(Object[] objs) {
		StringBuilder builder = new StringBuilder();
		builder.append('{');
		for (int i = 0; i < objs.length; i++) {
			builder.append(objs[i]).append(", ");
		}
		builder.setLength(builder.length() - 2);
		builder.append('}');
		return builder.toString();
	}
	
	public static <C> C newInstance(Class<C> clazz) {
		try {
			return clazz.newInstance();
		} catch (Exception e) {
			throw new RuntimeException("Failed to new instance : " + e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
    public static <C> C newInstance(String className) {
		try {
			Class<?> clazz = Class.forName(className);
			return (C)clazz.newInstance();
		} catch (Exception e) {
			throw new RuntimeException("Failed to new instance : " + e.getMessage());
		}
	}
}
