package org.afc.swagger;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import org.afc.spring.mvc.LocalDateControllerAdvice;
import org.afc.swagger.docs.SwaggerDocCustomHomeConfiguration;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import springfox.documentation.spring.web.plugins.Docket;

@Configuration
@Import({ SwaggerDocCustomHomeConfiguration.class })
public class DefaultSwaggerConfiguration {

	@Bean
	public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
		return new MappingJackson2HttpMessageConverter(mappingJackson2HttpObjectMapper());
	}

	@Bean
	public ObjectMapper mappingJackson2HttpObjectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		mapper.setSerializationInclusion(Include.NON_NULL);
		return mapper;
	}

	@Bean
	public Object configureDocket(Docket docket) {
		docket.directModelSubstitute(LocalDate.class, java.sql.Date.class);
		docket.directModelSubstitute(LocalDateTime.class, java.util.Date.class);
		docket.directModelSubstitute(LocalTime.class, java.sql.Timestamp.class);
		docket.directModelSubstitute(ZonedDateTime.class, java.util.Date.class);		
		return "configureDocket";
	}
	
	@Bean
	public LocalDateControllerAdvice localDateControllerAdvice() {
		return new LocalDateControllerAdvice();
	}
}
