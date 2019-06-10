package org.afc.filter;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode(callSuper = true)
public class InAttributeFilter<T> extends AbstractStringAttributeFilter<T> {

	protected List<String> values;

	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	protected BiFunction<T, String, String> getter;
	
	public InAttributeFilter(String name, String value, BiFunction<T, String, String> getter) {
		super(name, value, getter);
		this.values = Arrays.asList(value.split(","));
	}

	@Override
	protected boolean filter(String attribute, String value) {
		return values.contains(attribute);			
	}	
}

