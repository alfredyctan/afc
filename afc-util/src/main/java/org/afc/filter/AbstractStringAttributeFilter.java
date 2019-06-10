package org.afc.filter;

import java.util.function.BiFunction;

import org.afc.util.AutoString;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public abstract class AbstractStringAttributeFilter<T> implements AttributeFilter<T> {

	protected String name;
	
	protected String value;

	@AutoString.Hide
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	protected BiFunction<T, String, String> getter;
	
	public AbstractStringAttributeFilter(String name, String value, BiFunction<T, String, String> getter) {
		this.name = name;
		this.value = value;
		this.getter = getter;
	}

	@Override
	public boolean filter(T attributes) {
		String attribute = getter.apply(attributes, name);
		if (attribute == null) {
			return false;
		}
		return filter(attribute, value);			
	}
	
	protected abstract boolean filter(String attribute, String value);
}
