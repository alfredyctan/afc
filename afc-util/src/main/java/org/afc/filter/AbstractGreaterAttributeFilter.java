package org.afc.filter;

import java.util.function.BiFunction;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractGreaterAttributeFilter<T, C extends Comparable> extends AbstractComparableAttributeFilter<T, C> {

	public AbstractGreaterAttributeFilter(String name, String value, BiFunction<T, String, C> getter) {
		super(name, value, getter);
	}

	@Override
	protected boolean doFilter(C attribute, C value) {
		
		return attribute.compareTo(value) > 0;
	}
}
