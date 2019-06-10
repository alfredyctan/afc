package org.afc.logging;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SDCWebContext {

	@Bean
	public SDCFilterRegistrationBean sdcFilterRegistrationBean() {
		return new SDCFilterRegistrationBean();
	}

}