package org.afc.util;

import java.util.Enumeration;
import java.util.List;

public class ListEnumeration<T> implements Enumeration<T> {

	private final List<T> list;

	private final int size;

	private int cursor;

	public ListEnumeration(List<T> list) {
		this.list = list;
		this.size = list.size();
		this.cursor = 0;
	}

	public boolean hasMoreElements() {
		return (cursor < size);
	}

	public T nextElement() {
		return (T) list.get(cursor++);
	}
}
