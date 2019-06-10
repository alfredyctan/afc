package org.afc.swagger.docs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.swagger.web.SwaggerResourcesProvider;

@Configuration
public class SwaggerDocCustomHomeConfiguration {
	
	@Bean
	public CustomHomeApiResourceController customHomeApiResourceController(SwaggerResourcesProvider swaggerResources) {
		return new CustomHomeApiResourceController(swaggerResources);
	}

	@Bean
	public CustomHomeController customHomeController() {
		return new CustomHomeController();
	}

	@Bean
	public CustomHomeSwagger2Controller customHomeSwagger2Controller() {
		return new CustomHomeSwagger2Controller();
	}

	@Bean
	public CustomHomeWebMvcConfigurer customHomeWebMvcConfigurer() {
		return new CustomHomeWebMvcConfigurer();
	}
}

