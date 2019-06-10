package org.afc.util;

import java.util.Enumeration;

public class ArrayEnumeration<T> implements Enumeration<T> {

	private final int size;

	private int cursor;

	private final T[] array;

	public ArrayEnumeration(T[] array) {
		this.array = array;
		this.size = array.length;
		this.cursor = 0;
	}

	public boolean hasMoreElements() {
		return (cursor < size);
	}

	public T nextElement() {
		return (T) array[cursor++];
	}
}
