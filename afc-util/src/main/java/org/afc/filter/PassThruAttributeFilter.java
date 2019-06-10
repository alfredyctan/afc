package org.afc.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class PassThruAttributeFilter<T> implements AttributeFilter<T> {

	private static final Logger logger = LoggerFactory.getLogger(PassThruAttributeFilter.class);
	
	@Override
	public boolean filter(T attributes) {
		logger.info("passthrough : [{}]", attributes);
		return true;
	}
}

