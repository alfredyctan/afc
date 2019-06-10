package org.afc.servlet;

import org.afc.AFCException;

public class ServletContentException extends AFCException {

	private static final long serialVersionUID = 8347252836068449002L;

	private int status; 

	public ServletContentException(int status, String message) {
		super(message);
		this.status = status;
	}

	public int getStatus() {
		return status;
	}
}
