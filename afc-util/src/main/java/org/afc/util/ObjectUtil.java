package org.afc.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.Arrays;

public final class ObjectUtil {

	public static boolean isAllNull(Object... objs) {
		if (objs == null) {
			return true;
		}
		for (Object obj : objs) {
			if (obj != null) {
				return false;
			}
		}

		return true;
	}

	public static boolean isAllNotNull(Object... objs) {
		if (objs == null) {
			return false;
		}
		for (Object obj : objs) {
			if (obj == null) {
				return false;
			}
		}
		return true;
	}

	public static boolean isAnyoneNull(Object... objs) {
		if (objs == null) {
			return true;
		}
		for (Object obj : objs) {
			if (obj == null) {
				return true;
			}
		}
		return false;
	}

	public static boolean isAnyoneNotNull(Object... objs) {
		if (objs == null) {
			return false;
		}
		for (Object obj : objs) {
			if (obj != null) {
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public static <C> C cast(Object obj) {
		return (C) obj;
	}

	public static <C> Class<C> forName(String className) {
		try {
			return (Class<C>) Class.forName(className);
		} catch (Exception e) {
			throw new RuntimeException("Failed to load class : " + e.getMessage());
		}
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
			return (C) clazz.newInstance();
		} catch (Exception e) {
			throw new RuntimeException("Failed to new instance : " + e.getMessage());
		}
	}

	public static <C> Constructor<C> constructor(Class<C> clazz, Class<?>... parameterTypes) {
		try {
			return clazz.getConstructor(parameterTypes);
		} catch (Exception e) {
			throw new RuntimeException("Failed to new instance : " + e.getMessage());
		}
	}

	public static <C> C newInstance(Constructor<C> constructor, Object... args) {
		try {
			constructor.setAccessible(true);
			return constructor.newInstance(args);
		} catch (Exception e) {
			throw new RuntimeException("Failed to new instance : " + e.getMessage());
		}
	}

	public static <C> C newInstance(Class<C> clazz, Object... args) {
		try {
			Constructor<C> clazzes[] = (Constructor<C>[]) clazz.getConstructors();
			for (Constructor<C> constructor : clazzes) {
				if (match(constructor.getParameterTypes(), args)) {
					return newInstance(constructor, args);
				}
			}
			throw new RuntimeException("no suitable constructor found for " + Arrays.toString(args));
		} catch (Exception e) {
			throw new RuntimeException("Failed to new instance : " + e.getMessage());
		}
	}

	public static <A extends Annotation> A[] searchAnnotation(Class<?> clazz, Class<A> annotationClass) {
		return clazz.getPackage().getAnnotationsByType(annotationClass);
	}

	private static boolean match(Class<?>[] parameterTypes, Object[] args) {
		if (parameterTypes.length != args.length) {
			return false;
		}
		for (int i = 0; i < parameterTypes.length; i++) {
			if (!parameterTypes[i].isAssignableFrom(args[i].getClass())) {
				return false;
			}
		}
		return true;
	}
}