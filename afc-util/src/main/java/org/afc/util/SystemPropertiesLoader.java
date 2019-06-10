package org.afc.util;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.afc.AFCException;

public class SystemPropertiesLoader {

	private static final Logger logger = LoggerFactory.getLogger(SystemPropertiesLoader.class);

	public static void load(String file) {
		try {
			Properties properties = new Properties();
			properties.load(SystemPropertiesLoader.class.getResourceAsStream("/" + file));
			properties.entrySet().forEach(e -> {
				if (!e.getKey().toString().trim().startsWith("#")) {
					System.setProperty(e.getKey().toString(), e.getValue().toString());
					logger.info("{}={} loaded", e.getKey().toString(), e.getValue().toString());
				}
			});
		} catch (IOException e) {
			throw new AFCException("failed to load properties file " + file);
		}
	}
}
