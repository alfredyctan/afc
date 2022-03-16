package org.afc;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class ClasspathException extends AFCException {

	private static final long serialVersionUID = -7652421911151955186L;

	public ClasspathException(String message) {
		this(message, null);
	}

	public ClasspathException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
