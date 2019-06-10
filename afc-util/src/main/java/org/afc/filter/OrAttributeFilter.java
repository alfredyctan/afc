package org.afc.filter;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode
public class OrAttributeFilter<T> implements AttributeFilter<T> {

	private List<AttributeFilter<T>> filters;

	public OrAttributeFilter(List<AttributeFilter<T>> filters) {
		this.filters = filters;
	}

	@Override
	public boolean filter(T attributes) {
		if (filters == null || filters.size() == 0) {
			return true;
		}
		for (AttributeFilter<T> filter : filters) {
			if (filter.filter(attributes)) {
				return true;
			}
		}
		return false;
	}
}
