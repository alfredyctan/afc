package org.afc.util;

import java.util.function.Consumer;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;

public class ModelUtil {

	private static final ThreadLocal<ModelMapper> modelMapper = mapper(m -> {});

	public static ThreadLocal<ModelMapper> mapper(Consumer<ModelMapper> customizer) {
		return ThreadLocal.withInitial(() -> {
			ModelMapper modelMapper = new ModelMapper();
			modelMapper.getConfiguration().setFieldMatchingEnabled(true);
			modelMapper.getConfiguration().setFieldAccessLevel(AccessLevel.PRIVATE);
			customizer.accept(modelMapper);
			return modelMapper;
		});
	}
	
	public static <T> T map(Object source, Class<T> clazz) {
		return modelMapper.get().map(source, clazz);
	}

	public static <T> T map(T source, T target) {
		modelMapper.get().map(source, target);
		return target;
	}
}
