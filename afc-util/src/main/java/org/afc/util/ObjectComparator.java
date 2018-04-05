package org.afc.util;

import java.lang.reflect.Field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ObjectComparator {

	private static final Logger logger = LoggerFactory.getLogger(ObjectComparator.class); 
	
	private static boolean silent = true;
	
	public static class Member {
		
		Class<?> type;
		
		String name;

		public Member(Class<?> type, String name) {
			this.type = type;
			this.name = name;
		}
	}
	
	private Class<?> clazz;

	private FieldComparator[] fieldComparators; 
	
	public ObjectComparator(Class<?> clazz, Member... members) {
		this.clazz = clazz;
		this.fieldComparators = new FieldComparator[members.length];
		
		for (int i = 0; i < members.length; i++) {
			try {
				Field field = clazz.getDeclaredField(members[i].name);
				field.setAccessible(true);
				fieldComparators[i] = createFieldComparator(members[i].type, field); 
			} catch(Exception e) {
				e.printStackTrace();
				throw new RuntimeException("Error on initializing FieldComparator for [" + members[i] + ']', e);
			}
		}
	}

	public boolean compare(Object o1, Object o2) {
		if (o1 == o2) {
			return true;
		}
		if (o1 == null || o2 == null) {
			return false;
		}
		
		if (!o1.getClass().equals(o2.getClass())) {
			return false;
		}

		try {
			for (FieldComparator comparator:fieldComparators) {
				// check if equal, continue to check next field
				if (comparator.compare(o1, o2)) {
					continue; 
				}  
				
				// now is not equal
				if (!silent) {
					throw new ObjectUnequalException(clazz.getName() + '.' + comparator.getField().getName()+ " is not equal. " + comparator.getField().get(o1) + " != " + comparator.getField().get(o2));
				}

				// now is not equal and log trace
				if (logger.isTraceEnabled()) {
					logger.trace("{}.{} is not equal. {} != {}", new Object[] {clazz.getName(), comparator.getField().getName(), comparator.getField().get(o1), comparator.getField().get(o2)});
				}
				
				// return false and not checking next field
				return false;
			}
		} catch (IllegalArgumentException e) {
			if (!silent) {
				throw new ObjectUnequalException(e);
			} else {
				logger.trace("exception on checking field.", e);
			}
		} catch (IllegalAccessException e) {
			if (!silent) {
				throw new ObjectUnequalException(e);
			} else {
				logger.trace("exception on checking field.", e);
			}
		}
		return true;
	}
	
	private static FieldComparator createFieldComparator(Class<?> fieldType, Field field) {
		if (fieldType.equals(boolean.class)) {
			return new BooleanFieldComparator(field);
		} else if (fieldType.equals(byte.class)) {
			return new ByteFieldComparator(field);
		} else if (fieldType.equals(char.class)) {
			return new CharFieldComparator(field);
		} else if (fieldType.equals(double.class)) {
			return new DoubleFieldComparator(field);
		} else if (fieldType.equals(float.class)) {
			return new FloatFieldComparator(field);
		} else if (fieldType.equals(int.class)) {
			return new IntFieldComparator(field);
		} else if (fieldType.equals(long.class)) {
			return new LongFieldComparator(field);
		} else if (fieldType.equals(short.class)) {
			return new ShortFieldComparator(field);
		} else {
			return new ObjectFieldComparator(field);
		}
	}

	public static void setSilent(boolean silent) {
		ObjectComparator.silent = silent;
	}
		
	private static interface FieldComparator {
		
		public boolean compare(Object o1, Object o2) throws IllegalArgumentException, IllegalAccessException;

		public Field getField();

	}

	private static class BooleanFieldComparator implements FieldComparator {

		private Field field;

		public BooleanFieldComparator(Field field) {
			this.field = field;
		}
		
		public Field getField() {
			return field;
		}

		public boolean compare(Object o1, Object o2) throws IllegalArgumentException, IllegalAccessException {
			return (field.getBoolean(o1) == field.getBoolean(o2));
		}
	}

	private static class ByteFieldComparator implements FieldComparator {

		private Field field;

		public ByteFieldComparator(Field field) {
			this.field = field;
		}
		
		public Field getField() {
			return field;
		}

		public boolean compare(Object o1, Object o2) throws IllegalArgumentException, IllegalAccessException {
			return (field.getByte(o1) == field.getByte(o2));
		}
	}

	private static class CharFieldComparator implements FieldComparator {

		private Field field;

		public CharFieldComparator(Field field) {
			this.field = field;
		}
		
		public Field getField() {
			return field;
		}

		public boolean compare(Object o1, Object o2) throws IllegalArgumentException, IllegalAccessException {
			return (field.getChar(o1) == field.getChar(o2));
		}
	}

	private static class DoubleFieldComparator implements FieldComparator {

		private Field field;

		public DoubleFieldComparator(Field field) {
			this.field = field;
		}
		
		public Field getField() {
			return field;
		}

		public boolean compare(Object o1, Object o2) throws IllegalArgumentException, IllegalAccessException {
			return (field.getDouble(o1) == field.getDouble(o2));
		}
	}

	private static class FloatFieldComparator implements FieldComparator {

		private Field field;

		public FloatFieldComparator(Field field) {
			this.field = field;
		}
		
		public Field getField() {
			return field;
		}

		public boolean compare(Object o1, Object o2) throws IllegalArgumentException, IllegalAccessException {
			return (field.getFloat(o1) == field.getFloat(o2));
		}
	}

	private static class IntFieldComparator implements FieldComparator {

		private Field field;

		public IntFieldComparator(Field field) {
			this.field = field;
		}
		
		public Field getField() {
			return field;
		}

		public boolean compare(Object o1, Object o2) throws IllegalArgumentException, IllegalAccessException {
			return (field.getInt(o1) == field.getInt(o2));
		}
	}

	private static class LongFieldComparator implements FieldComparator {

		private Field field;

		public LongFieldComparator(Field field) {
			this.field = field;
		}
		
		public Field getField() {
			return field;
		}

		public boolean compare(Object o1, Object o2) throws IllegalArgumentException, IllegalAccessException {
			return (field.getLong(o1) == field.getLong(o2));
		}
	}

	private static class ShortFieldComparator implements FieldComparator {

		private Field field;

		public ShortFieldComparator(Field field) {
			this.field = field;
		}
		
		public Field getField() {
			return field;
		}

		public boolean compare(Object o1, Object o2) throws IllegalArgumentException, IllegalAccessException {
			return (field.getShort(o1) == field.getShort(o2));
		}
	}

	private static class ObjectFieldComparator implements FieldComparator {

		private Field field;

		public ObjectFieldComparator(Field field) {
			this.field = field;
		}
		
		public Field getField() {
			return field;
		}

		public boolean compare(Object o1, Object o2) throws IllegalArgumentException, IllegalAccessException {
			Object field1 = field.get(o1);
			Object field2 = field.get(o2);
			
			if (field1 == null && field2 != null) {
				return false;
			}

			if ((field1 != null) && !field1.equals(field2)) {
				return false;
			}
			
			return true;
		}
	}
}
