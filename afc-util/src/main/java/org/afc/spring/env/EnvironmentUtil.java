package org.afc.spring.env;

import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.source.ConfigurationPropertySource;
import org.springframework.boot.env.RandomValuePropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;

import org.afc.util.ObjectUtil;

public class EnvironmentUtil {

	private static final Logger logger = LoggerFactory.getLogger(EnvironmentUtil.class);
	
	public static Map<String, Object> properties(ConfigurableEnvironment env) {
		Map<String, Object> properties = new TreeMap<>();
		env.getPropertySources().forEach(propertySource -> {
			properties(properties, propertySource.getName(), propertySource.getSource());
		});
		return properties;
	}
	
	private static Map<String, Object> properties(Map<String, Object> properties, String name, Object propertySource) {
		if (propertySource instanceof Map) {
			properties.putAll(ObjectUtil.cast(propertySource));
		} else if (propertySource instanceof Random) {
			properties.put(name, propertySource);
		} else if (propertySource instanceof PropertiesPropertySource) {
			PropertiesPropertySource source = ObjectUtil.cast(propertySource);
			properties.putAll(source.getSource());
		} else if (propertySource instanceof MapPropertySource) {
			MapPropertySource source = ObjectUtil.cast(propertySource);
			properties.putAll(source.getSource());
		} else if (propertySource instanceof PropertySource) {
			PropertySource<Object> source = ObjectUtil.cast(propertySource);
			properties(properties, source.getName(), source.getSource());
		} else if (propertySource instanceof RandomValuePropertySource) {
			RandomValuePropertySource source = ObjectUtil.cast(propertySource);
			properties.put(source.getName(), source.getSource());
		} else if (propertySource instanceof Iterable) {
			for (ConfigurationPropertySource source : ((Iterable<ConfigurationPropertySource>)propertySource)) {
				properties(properties, source.getClass().getName(), source.getUnderlyingSource());
			}
		} else {
        	logger.debug("property source not supported, class:" + propertySource.getClass().getName());
		}
		return properties;
	}
	
	public static void infoProperties(ConfigurableEnvironment env) {
		properties(env).forEach((k, v) -> {
			logger.info("[{}]=[{}]", k, v);
		}); 
	}

	public static void debugProperties(ConfigurableEnvironment env) {
		properties(env).forEach((k, v) -> {
			logger.debug("[{}]=[{}]", k, v);
		}); 
	}

	public static void traceProperties(ConfigurableEnvironment env) {
		properties(env).forEach((k, v) -> {
			logger.trace("[{}]=[{}]", k, v);
		}); 
	}
}
