package org.afc.json;

import java.util.LinkedList;
import java.util.List;

import org.afc.AFCException;

public class JsonValidationException extends AFCException {

	private static final long serialVersionUID = 2506543154734420913L;

	private List<String> details;
	
	public JsonValidationException(String message) {
		this(message, (Throwable)null);
	}

	public JsonValidationException(String message, List<String> details) {
		super(message);
		this.details = details;
	}

	public JsonValidationException(String message, Throwable throwable) {
		super(message, throwable);
		this.details = new LinkedList<>();
	}

	public JsonValidationException(Throwable throwable) {
		this(null, throwable);
	}
	
	public List<String> getDetails() {
		return details;
	}
}
