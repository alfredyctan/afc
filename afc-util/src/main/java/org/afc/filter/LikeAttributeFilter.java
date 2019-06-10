package org.afc.filter;

import java.util.function.BiFunction;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode(callSuper = true)
public class LikeAttributeFilter<T> extends AbstractStringAttributeFilter<T> {

	public LikeAttributeFilter(String name, String value, BiFunction<T, String, String> getter) {
		super(name, value, getter);
	}

	@Override
	protected boolean filter(String attribute, String value) {
		return attribute.contains(value);
	}
}

