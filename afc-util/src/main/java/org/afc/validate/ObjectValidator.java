package org.afc.validate;

import java.util.List;

public interface ObjectValidator<O> {

	/**
	 * validate the inbound object
	 * 
	 * @param inbound - object to be validated
	 * @return should continue next validator if any
	 * @throws ValidationException - validation error details
	 */
	public boolean validate(O inbound) throws ValidationException;
	

	default void notEmptyList(List<?> list, String message) {
		if (list == null || list.size() < 1) {
			throw new ValidationException(message);
		}
	}
	
	default <T> void required(T t, String failedMessage) {
		if (t == null) {
			throw new ValidationException(failedMessage);
		}
	}

	default <T> void decline(T t, String failedMessage) {
		if (t != null) {
			throw new ValidationException(failedMessage);
		}
	}
}
