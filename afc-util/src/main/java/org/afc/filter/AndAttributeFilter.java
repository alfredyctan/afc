package org.afc.filter;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode
public class AndAttributeFilter<T> implements AttributeFilter<T> {

	private List<AttributeFilter<T>> filters;

	public AndAttributeFilter(List<AttributeFilter<T>> filters) {
		this.filters = filters;
	}

	@Override
	public boolean filter(T attributes) {
		if (filters != null) {
			for (AttributeFilter<T> filter : filters) {
				if (!filter.filter(attributes)) {
					return false;
				}
			}
		}
		return true;
	}
}
