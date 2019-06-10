package org.afc.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class AutoString {

	private static Set<Class<?>> SIMPLE = new HashSet<>(
		Arrays.asList(
    		BigDecimal.class, 
    		Character.class,
			String.class, 
    		Boolean.class,
    		Long.class, 
    		Short.class, 
    		Integer.class,
    		Float.class,
    		Double.class, 
    		Byte.class,
    		Date.class,
    		java.sql.Date.class,
    		Timestamp.class,
    		Instant.class,
    		LocalDate.class,
    		LocalDateTime.class,
    		OffsetDateTime.class,
    		ZonedDateTime.class
	    )
	);
	
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	public @interface Hide {

	}
	
	private static final Map<Class<?>, Field[]> FIELDS  = new ConcurrentHashMap<>();

	private Object target;
	
	public AutoString(Object target) {
		this.target = target;
	}
	
	@Override
	public String toString() {
		return of(target);
	}
	
	public static String of(Object object) {
		return (object == null) ? null : build(new StringBuilder(), object).toString();
	}

	@SuppressWarnings({ "rawtypes" })
	private static StringBuilder build(StringBuilder builder, Object object) {
		if (isSimpleToString(object.getClass())) {
			builder.append(object);
		} else if (Iterable.class.isAssignableFrom(object.getClass())) {
			buildIterable(builder, object);
		} else if (object.getClass().isArray()) {
			Object[] value = (Object[])object;
			buildArray(builder, value);
		} else if (Map.class.isAssignableFrom(object.getClass())) {
			Map value = (Map) object;
			buildMap(builder, value); 
		} else {
			prepare(object.getClass());
			builder.append(object.getClass().getSimpleName()).append('(');
			int initialLen = builder.length();
			for (Field field : FIELDS.get(object.getClass())) {
				Object member = getValue(field, object);
				if (member == null) {
					continue;
				}			
				builder.append(field.getName()).append('=');
				build(builder, getValue(field, object));
				builder.append(", ");
			}
			if (builder.length() > initialLen) {
				builder.setLength(builder.length() - 2);
			}
			builder.append(')');
		}
		return builder;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static void buildMap(StringBuilder builder, Map value) {
		builder.append("{");
		value.forEach((k, v) -> { 
			build(builder, k).append("=");
			build(builder, v).append(", ");
		});
		builder.setLength(builder.length() - 2);
		builder.append("}");
	}

	private static void buildArray(StringBuilder builder, Object[] value) {
		builder.append("[");
		for (Object v : value) {
			build(builder, v).append(", ");
		}
		builder.setLength(builder.length() - 2);
		builder.append("]");
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static void buildIterable(StringBuilder builder, Object object) {
		Iterable value = (Iterable)object;
		builder.append("[");
		value.forEach(v -> {
			build(builder, v).append(", ");
		});
		builder.setLength(builder.length() - 2);
		builder.append("]");
	}
	
	@SuppressWarnings("unchecked")
	private static <T> T getValue(Field field, Object object) {
		try {
			return (T)field.get(object);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			return null;
		}
	}
	
	private static Field[] prepare(Class<?> clazz) {
		Field[] fields = FIELDS.get(clazz);
		if (fields != null) {
			return fields;
		}
		
		List<Field> preparing = new ArrayList<>();
		Class<?> clazz0 = clazz;
		while (clazz0 != null && !clazz0.equals(Object.class)) {
			for (Field field : clazz0.getDeclaredFields()) {
				if (!Modifier.isStatic(field.getModifiers()) && field.getAnnotation(Hide.class) == null) {
					field.setAccessible(true);
					preparing.add(field);
				}
			}
			clazz0  = clazz0.getSuperclass();
		}
		fields = preparing.toArray(new Field[preparing.size()]);
		FIELDS.put(clazz, fields);
		return fields;
	}
	
	private static boolean isSimpleToString(Class<?> type) {
	    return SIMPLE.contains(type) || type.isEnum() || (type.isPrimitive() && type != void.class);  
	}
}
