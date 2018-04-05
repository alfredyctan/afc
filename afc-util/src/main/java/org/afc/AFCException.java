package org.afc;

public class AFCException extends RuntimeException {

	private static final long serialVersionUID = -8232111281531434816L;

	public AFCException(String message) {
		super(message);
	}

	public AFCException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public AFCException(Throwable throwable) {
		super(throwable);
	}
}
