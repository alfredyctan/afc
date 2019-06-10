package org.afc.filter;

import java.util.function.BiFunction;
import java.util.regex.Pattern;

import org.afc.util.AutoString;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode(callSuper = true)
public class RegexAttributeFilter<T> extends AbstractStringAttributeFilter<T> {

	@AutoString.Hide
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	protected Pattern pattern;

	public RegexAttributeFilter(String name, String value, BiFunction<T, String, String> getter) {
		super(name, value, getter);
		this.pattern = Pattern.compile(value);
	}

	@Override
	protected boolean filter(String attribute, String value) {
		return pattern.matcher(attribute).find();
	}	
}
