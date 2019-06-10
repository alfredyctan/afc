package org.afc.json.everit;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.everit.json.schema.Schema;
import org.everit.json.schema.SchemaException;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.afc.json.JsonSchemaValidator;
import org.afc.json.JsonValidationException;
import org.afc.util.ExceptionUtil;
import org.afc.util.FileUtil;

public class EveritJsonSchemaValidator implements JsonSchemaValidator {

	private static final Logger logger = LoggerFactory.getLogger(EveritJsonSchemaValidator.class);

	private String base;

	private Map<String, Schema> schemas;
	
	public EveritJsonSchemaValidator() {
		this(null);
	}
	
	public EveritJsonSchemaValidator(String base) {
		this.schemas = new ConcurrentHashMap<>();
		setBase(base);
	}

	@Override
	public void validate(String json, String schemaRef) {
		Schema schema = initSchema(schemaRef);
		try {
			schema.validate(new JSONObject(json));
		} catch (ValidationException e) {
			logger.info("invalid json message, reason:[{}], details:{}", e.getMessage(), e.getAllMessages());
			throw new JsonValidationException(e.getMessage(), e.getAllMessages());
		}
	}

	private Schema initSchema(String schemaRef) {
		if (schemaRef == null || schemaRef.isEmpty()) {
			throw new JsonValidationException("no schema reference is specified");
		}
		Schema schema = schemas.get(schemaRef);
		if (schema == null) {
			synchronized(schemas) {
				schema = load(schemaRef);
				schemas.put(schemaRef, schema);
			}
		}
		return schema;
	}

	private Schema load(String schemaRef) {
		int poundIdx = schemaRef.indexOf('#');
		String fragment;
		String toBeQueried;
		if (poundIdx == -1) {
			toBeQueried = schemaRef;
			fragment = "";
		} else {
			fragment = schemaRef.substring(poundIdx);
			toBeQueried = schemaRef.substring(0, poundIdx);
		}

		try (InputStream in = FileUtil.resolveResourceAsStream(base + toBeQueried)) {
			if (in == null) {
				throw new JsonValidationException("schema not found in [" + base + toBeQueried + ']');
			}
			JSONObject schemaJson = new JSONObject(new JSONTokener(in));
			if (!fragment.isEmpty()) {
				String[] path = fragment.split("/");
				if ((path[0] == null) || !path[0].startsWith("#")) {
					throw new IllegalArgumentException("JSON pointers in [" + schemaRef + "] must start with a '#'");
				}

				if (!"#".equals(fragment)) {
					schemaJson = (JSONObject) new org.json.JSONPointer(fragment).queryFrom(schemaJson);
				}
			}
			logger.info("json schema loaded: {}", schemaJson);

			return SchemaLoader.builder()
				.schemaJson(schemaJson)
				.httpClient(new FileSchemaClient(base))
				.addFormatValidator(new DateFormatValidator())
				.addFormatValidator(new TimeFormatValidator())
				.build()
				.load()
				.build();
		} catch (SchemaException e) {
			throw new JsonValidationException(ExceptionUtil.unwrap(e).getMessage(), e);
		} catch (UncheckedIOException e) {
			throw new JsonValidationException(ExceptionUtil.unwrap(e).getMessage(), e);
		} catch (IOException e) {
			throw new JsonValidationException(e);
		}
	}
	
	public void setBase(String base) {
		this.base = (base == null) ? "" : (!base.isEmpty() && !base.endsWith("/")) ? base  + '/' : base;
	}
}
