package org.afc.validate;

import org.afc.AFCException;

public class ValidationException extends AFCException {

	private static final long serialVersionUID = -251920195643413870L;

	public ValidationException(String message) {
		super(message);
	}

	public ValidationException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public ValidationException(Throwable throwable) {
		super(throwable);
	}
}
