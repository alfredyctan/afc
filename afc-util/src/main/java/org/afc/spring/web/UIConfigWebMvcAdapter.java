package org.afc.spring.web;

import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class UIConfigWebMvcAdapter implements WebMvcConfigurer {

	private static final Logger logger = LoggerFactory.getLogger(UIConfigWebMvcAdapter.class);

	@Autowired
	private ResourceProperties resourceProperties;

	@Value("${sys.env}")
	private String env;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/config/**")
			.addResourceLocations("classpath:/ui/" + env + "/", "classpath:/ui/common/")
			.setCachePeriod(getSeconds(resourceProperties.getCache().getPeriod()))
			.setCacheControl(resourceProperties.getCache().getCachecontrol().toHttpCacheControl());
	}

	private static Integer getSeconds(Duration cachePeriod) {
		return (cachePeriod == null ? null : (int) cachePeriod.getSeconds());
	}
}
