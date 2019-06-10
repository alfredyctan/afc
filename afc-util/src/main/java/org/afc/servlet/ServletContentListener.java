package org.afc.servlet;

import javax.servlet.http.HttpServletRequest;

public interface ServletContentListener {

	public void onContent(HttpServletRequest servletRequest, byte[] content);
	
}
