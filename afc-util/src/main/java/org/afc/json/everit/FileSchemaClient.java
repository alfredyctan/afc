package org.afc.json.everit;

import java.io.InputStream;

import org.everit.json.schema.loader.SchemaClient;
import org.everit.json.schema.loader.internal.DefaultSchemaClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.afc.util.FileUtil;

public class FileSchemaClient implements SchemaClient {

	private static final Logger logger = LoggerFactory.getLogger(FileSchemaClient.class);

	
	private SchemaClient delegate;

	private String base;

	public FileSchemaClient(String base) {
		this.delegate = new DefaultSchemaClient();
		this.base = base;
	}

	@Override
	public InputStream get(String url) {
		logger.info("sub-schema loaded: {}", FileUtil.readFileAsString(base + url));
		InputStream in = FileUtil.resolveResourceAsStream(base + url);
		if (in != null) {
			return in;
		} else {
			logger.info("[{}] schema resource not found, fallback to default URL schema client", base + url);
			return delegate.get(base + url);
		}
	}
}
