package org.afc.jackson;

import java.io.StringWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class JacksonUtil {

	private static final Logger logger = LoggerFactory.getLogger(JacksonUtil.class);

	private static final ThreadLocal<ObjectMapper> objectMapper = new ThreadLocal<ObjectMapper>() {

		protected ObjectMapper initialValue() {
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.registerModule(new JavaTimeModule());
			objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
			objectMapper.setSerializationInclusion(Include.NON_NULL);
			return objectMapper;
		}
	};

	public static <T> String toJsonString(T t) {
		return toJsonString(t, new SimpleFilterProvider().setFailOnUnknownId(false));
	}

	public static <T> String toJsonString(T t, FilterProvider filter) {
		try {
			StringWriter writer = new StringWriter();
			ObjectMapper mapper = (filter == null) ? objectMapper.get() : objectMapper.get().setFilterProvider(filter);
			mapper.writerWithDefaultPrettyPrinter().writeValue(writer, t);
			return writer.toString();
		} catch (Exception e) {
			logger.info("fail to serialize object, {}", e.getMessage());
			logger.debug("details", e);
			return null;
		}
	}
	
	
	
	public static <T> T fromJsonString(String json, Class<T> clazz) {
		try {
			return objectMapper.get().readerFor(clazz).readValue(json);
		} catch (Exception e) {
			logger.info("fail to deserialize json, {}", e.getMessage());
			logger.debug("details", e);
			return null;
		}
	}
}
