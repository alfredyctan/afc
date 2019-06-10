package org.afc.logging;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SDCFilter implements Filter {

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		String sdc = request.getHeader(SDC.SDC);
		SDC.set(sdc != null ? sdc : SDC.hash(req));
		
		chain.doFilter(req, res);
	
		HttpServletResponse response = (HttpServletResponse) res;
		response.setHeader(SDC.SDC, sdc);
	}

	@Override
	public void destroy() {
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
	}

}