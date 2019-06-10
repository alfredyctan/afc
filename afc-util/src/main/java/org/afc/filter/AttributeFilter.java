package org.afc.filter;

import java.util.function.Supplier;

public interface AttributeFilter<T> {

	public boolean filter(T attributes);
	
	static <T> AttributeFilter<T> create(Supplier<AttributeFilter<T>> filter) {
		try {
			return filter.get();
		} catch (Exception e) {
			return null;
		}
	}
}
