package org.afc.swagger2.docs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class CustomHomeWebMvcConfigurer implements WebMvcConfigurer {
	
	@Value("${springfox.documentation.swagger.v2.home}")
	private String home;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
	    registry.addResourceHandler(home + "/**").addResourceLocations("classpath:/META-INF/resources/");
	}
	
}

