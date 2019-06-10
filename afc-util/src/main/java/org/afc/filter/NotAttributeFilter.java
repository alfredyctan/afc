package org.afc.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class NotAttributeFilter<T> implements AttributeFilter<T> {

	private static final Logger logger = LoggerFactory.getLogger(NotAttributeFilter.class);

	private AttributeFilter<T> delegate;
	
	public NotAttributeFilter(AttributeFilter<T> delegate) {
		this.delegate = delegate;
	}

	@Override
	public boolean filter(T attributes) {
		return !delegate.filter(attributes);
	}
}

