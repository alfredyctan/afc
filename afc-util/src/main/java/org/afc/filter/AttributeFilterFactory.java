package org.afc.filter;

public interface AttributeFilterFactory<T, F> {

	public AttributeFilter<T> create(F filter);

}
