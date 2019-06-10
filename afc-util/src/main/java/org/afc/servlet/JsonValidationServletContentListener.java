package org.afc.servlet;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpStatus;

import org.afc.json.JsonSchemaValidator;
import org.afc.json.JsonValidationException;

import lombok.Data;

@ConfigurationProperties("servlet.content-filter")
public class JsonValidationServletContentListener implements ServletContentListener {

	private static final Logger logger = LoggerFactory.getLogger(JsonValidationServletContentListener.class);

	private JsonSchemaValidator validator;

	private Map<String, String> schemas;

	public JsonValidationServletContentListener() {
	}
	
	public JsonValidationServletContentListener(JsonSchemaValidator validator) {
		this.validator = validator;
	}

	@Override
	public void onContent(HttpServletRequest servletRequest, byte[] content) {
		String schema = schemas.get(servletRequest.getServletPath());
		if (schema == null || schema.isEmpty()) {
			logger.error("no schema is found for [{}]", servletRequest.getServletPath());
			throw new ServletContentException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "json validation failed");
		}

		String json = new String(content);
		logger.debug("content:{}", json); 
		try {
			validator.validate(json, schema);
		} catch (JsonValidationException e) {
			throw new ServletContentException(HttpStatus.BAD_REQUEST.value(), e.getDetails().toString());
		}
	}

	public void setValidations(Map<String, Binding> validations) {
		schemas = new HashMap<>();
		validations.values().forEach(b -> schemas.put(b.getPath(), b.getSchema()));
	}

	@Data
	public static class Binding {
		
		private String path;
		
		private String schema;
		
	}
	
	public void setValidator(JsonSchemaValidator validator) {
		this.validator = validator;
	}
}
