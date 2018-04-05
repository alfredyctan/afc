package org.afc.logging;

import javax.servlet.Filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.stereotype.Component;
/**
 * 
 * add @ComponentScan("org.afc.logging") into bean context to enable the SDCFilter()
 * 
 * eg.
 * <pre>
 * @Configuration
 * @ComponentScan("org.afc.logging")
 * public class BeanContext {
 * }
 * </pre>
 * 
 * @author atyc
 *
 */
@Component
public class SDCFilterRegistrationBean extends FilterRegistrationBean {
	
	public SDCFilterRegistrationBean() {
		this(new SDCFilter());
	}

	public SDCFilterRegistrationBean(Filter filter) {
	    setFilter(filter);
	    addUrlPatterns("/*");
	    setOrder(1);
	}	
}
