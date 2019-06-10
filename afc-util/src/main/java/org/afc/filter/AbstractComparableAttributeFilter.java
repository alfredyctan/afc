package org.afc.filter;

import java.util.function.BiFunction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.EqualsAndHashCode;
import lombok.ToString;


@ToString
@EqualsAndHashCode
public abstract class AbstractComparableAttributeFilter<T, C extends Comparable> implements AttributeFilter<T> {

	private static final Logger logger = LoggerFactory.getLogger(AbstractComparableAttributeFilter.class);

	protected String name;
	
	protected C value;
	
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	protected BiFunction<T, String, C> getter;
	
	public AbstractComparableAttributeFilter(String name, String value, BiFunction<T, String, C> getter) {
		this.name = name;
		this.value = parse(value);
		this.getter = getter;
	}

	@Override
	public boolean filter(T attributes) {
		try {
			C attribute = getter.apply(attributes, name);
			if (attribute == null) {
				return false;
			}
			return doFilter(attribute, value);
		} catch (Exception e) {
			return false;
		} 
	}

	protected abstract C parse(String value);
	
	protected abstract boolean doFilter(C attribute, C value);
}
