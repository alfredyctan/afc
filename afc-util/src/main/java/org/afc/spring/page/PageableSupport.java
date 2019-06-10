package org.afc.spring.page;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.util.StringUtils;

import org.afc.AFCException;
import org.afc.util.StringUtil;

public class PageableSupport {

	private static final ThreadLocal<ModelMapper> modelMapper = mapper(m -> {});
	
	private static final String ASC = "asc";

	private static final String DESC = "desc";

	private static final int DEFAULT_PAGE_SIZE = 20;

	public static ThreadLocal<ModelMapper> mapper(Consumer<ModelMapper> customizer) {
		return ThreadLocal.withInitial(() -> {
			ModelMapper modelMapper = new ModelMapper();
			modelMapper.getConfiguration().setFieldMatchingEnabled(true);
			modelMapper.getConfiguration().setFieldAccessLevel(AccessLevel.PRIVATE);
			customizer.accept(modelMapper);
			return modelMapper;
		});
	}

	public static <T, S> T toSwaggerPage(S data, Class<T> swaggerPageClass) {
		return toSwaggerPage(data, swaggerPageClass, modelMapper);
	}
	
	public static <T, S> T toSwaggerPage(S data, Class<T> swaggerPageClass, ThreadLocal<ModelMapper> modelMapper) {
		try {
			return modelMapper.get().map(data, swaggerPageClass);
		} catch (Exception e) {
			throw new AFCException("error on copy properties", e);
		}
	}

	public static Pageable newPageable(Integer page, Integer size, List<String> sortParameters) {
		return newPageable(page, size, null, sortParameters);
	}

	public static Pageable newPageable(Integer page, Integer size, List<String> sortParameters, String defaultSortParameter) {
		return newPageable(page, size, null, sortParameters, defaultSortParameter);
	}

	public static Pageable newPageable(Integer page, Integer size, Integer defaultSize, List<String> sortParameters, String defaultSortParameter) {
		if (StringUtil.hasValue(defaultSortParameter)) {
			if (sortParameters == null) {
				sortParameters = new ArrayList<>();
			}
			sortParameters.add(defaultSortParameter);
		}
		return newPageable(page, size, defaultSize, sortParameters);
	}

	public static Pageable newPageable(Integer page, Integer size, Integer defaultSize, List<String> sortParameters) {
		if (!validPage(page)) {
			page = 0;
		}
		if (!validSize(size)) {
			size = validSize(defaultSize) ? defaultSize : DEFAULT_PAGE_SIZE;
		}
		if (sortParameters != null && sortParameters.size() > 0) {
			return PageRequest.of(page, size, parseParameterIntoSort(sortParameters));
		}
		return PageRequest.of(page, size);
	}

	private static boolean validSize(Integer size) {
		return size != null && size > 0;
	}

	private static boolean validPage(Integer page) {
		return page != null && page >= 0;
	}

	private static Sort parseParameterIntoSort(List<String> sortParameters) {
		List<Order> allOrders = new ArrayList<>();

		for (String part : resolveSortParameters(sortParameters)) {
			if (part == null) {
				continue;
			}
			String[] elements = part.split(",");
			Optional<Direction> direction = elements.length == 0 ? Optional.empty() : Direction.fromOptionalString(elements[elements.length - 1]);
			int lastIndex = direction.map(it -> elements.length - 1).orElseGet(() -> elements.length);
			for (int i = 0; i < lastIndex; i++) {
				toOrder(elements[i], direction).ifPresent(allOrders::add);
			}
		}

		return allOrders.isEmpty() ? Sort.unsorted() : Sort.by(allOrders);
	}

	private static List<String> resolveSortParameters(List<String> sortParameters) {
		sortParameters.removeAll(Arrays.asList("", null));
		List<String> resolvedSortParameters = new ArrayList<>();
		String parameters = String.join(",", sortParameters);
		String[] elements = parameters.split(",");
		String column, sort;
		for (int i = 0; i < elements.length; i++) {
			column = elements[i];
			if (i + 1 < elements.length) {
				sort = elements[i + 1];
				if (ASC.equalsIgnoreCase(sort) || DESC.equalsIgnoreCase(sort)) {
					i++;
					resolvedSortParameters.add(column + "," + sort);
					continue;
				}
			}
			resolvedSortParameters.add(column);
		}
		return resolvedSortParameters;
	}

	private static Optional<Order> toOrder(String property, Optional<Direction> direction) {
		if (!StringUtils.hasText(property)) {
			return Optional.empty();
		}
		return Optional.of(direction.map(it -> new Order(it, property)).orElseGet(() -> Order.by(property)));
	}
	
	public static <T> Page<T> emptyPage() {
		return new PageImpl<>(new LinkedList<>());
	}
}
