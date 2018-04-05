package org.afc.util;

public class ObjectUnequalException extends RuntimeException {

	private static final long serialVersionUID = -885525105345713446L;

	public ObjectUnequalException(String message) {
		super(message);
	}

	public ObjectUnequalException(Throwable cause) {
		super(cause);
	}

	public ObjectUnequalException(String message, Throwable cause) {
		super(message, cause);
	}
}
