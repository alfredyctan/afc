package org.afc.servlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServletContentFilter implements Filter {

	private static final Logger logger = LoggerFactory.getLogger(ServletContentFilter.class);
	
	private List<ServletContentListener> listeners;
	
	public ServletContentFilter() {
		this.listeners = new LinkedList<>();
	}
	
	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		 
		switch (request.getMethod()) {
			case "POST":
			case "PUT":
				logger.info("invoke servlet request filter for [{}]", request.getServletPath());
				
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				ServletInputStream in = request.getInputStream();
				while (in.read(buffer) != -1) {
					out.write(buffer);
				}

				byte[] content = out.toByteArray();
				try  {
					listeners.stream().forEach(listener -> listener.onContent(request, content));
					chain.doFilter(new ByteArrayHttpServletRequest(request, content) , res);
				} catch (ServletContentException e) {
					response.sendError(e.getStatus(), e.getMessage());
				}
				break;
			default:
				chain.doFilter(request, res);
		}
	}

	@Override
	public void destroy() {
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
	}

	public ServletContentFilter addServletContentListener(ServletContentListener listener) {
		listeners.add(listener);
		return this;
	}

	public ServletContentFilter setServletContentListeners(List<ServletContentListener> listeners) {
		listeners.addAll(listeners);
		return this;
	}
}