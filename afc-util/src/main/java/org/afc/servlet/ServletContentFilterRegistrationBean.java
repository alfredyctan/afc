package org.afc.servlet;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;

@ConfigurationProperties("servlet.content-filter")
public class ServletContentFilterRegistrationBean extends FilterRegistrationBean<ServletContentFilter> {

	public ServletContentFilterRegistrationBean(ServletContentFilter filter) {
	    setFilter(filter);
	}	

	public ServletContentFilterRegistrationBean(ServletContentFilter filter, int order) {
	    setFilter(filter);
	    setOrder(order);
	}	
	
	public ServletContentFilterRegistrationBean(ServletContentFilter filter, int order, String patterns) {
	    setFilter(filter);
	    setOrder(order);
	    addUrlPatterns(patterns);
	}	
}
