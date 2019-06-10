package org.afc.filter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.function.BiFunction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.afc.util.DateUtil;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class LesserAttributeFilter<T> implements AttributeFilter<T> {

	private static final Logger logger = LoggerFactory.getLogger(LesserAttributeFilter.class);

	private AttributeFilter<T> delegate;
	
	public LesserAttributeFilter(String name, String value, AttributeAccessor<T> accessor) {
		this.delegate = AttributeFilter.create(() -> new NumericFilter<>(name, value, accessor::getNumeric));
		
		if (this.delegate == null) {
			this.delegate = AttributeFilter.create(() -> new OffsetDateTimeFilter<>(name, value, accessor::getOffsetDateTime));
		}

		if (this.delegate == null) {
			this.delegate = AttributeFilter.create(() -> new ZonedDateTimeFilter<>(name, value, accessor::getZonedDateTime));
		}
		
		if (this.delegate == null) {
			this.delegate = AttributeFilter.create(() -> new LocalDateTimeFilter<>(name, value, accessor::getLocalDateTime));
		}
		if (this.delegate == null) {
			this.delegate = AttributeFilter.create(() -> new LocalDateFilter<>(name, value, accessor::getLocalDate));
		}

		if (this.delegate == null) {
			this.delegate = AttributeFilter.create(() -> new LocalTimeFilter<>(name, value, accessor::getLocalTime));
		}

		if (this.delegate == null) {
			this.delegate = AttributeFilter.create(() -> new StringFilter<>(name, value, accessor::getString));
		}
	}

	@Override
	public boolean filter(T attributes) {
		return delegate.filter(attributes);
	}
	
	@EqualsAndHashCode(callSuper = true)
	private static class StringFilter<T> extends AbstractStringAttributeFilter<T> {

		public StringFilter(String name, String value, BiFunction<T, String, String> getter) {
			super(name, value, getter);
		}

		@Override
		protected boolean filter(String attribute, String value) {
			return attribute.compareToIgnoreCase(value) < 0;
		}
	}

	@EqualsAndHashCode(callSuper = true)
	private static class NumericFilter<T> extends AbstractLesserAttributeFilter<T, BigDecimal> {

		public NumericFilter(String name, String value, BiFunction<T, String, BigDecimal> getter) {
			super(name, value, getter);
		}

		@Override
		protected BigDecimal parse(String value) {
			return new BigDecimal(value);
		}
	}

	@EqualsAndHashCode(callSuper = true)
	private static class LocalDateFilter<T> extends AbstractLesserAttributeFilter<T, LocalDate> {

		public LocalDateFilter(String name, String value, BiFunction<T, String, LocalDate> getter) {
			super(name, value, getter);
		}

		@Override
		protected LocalDate parse(String value) {
			return DateUtil.localDate(value);
		}
	}

	@EqualsAndHashCode(callSuper = true)
	private static class LocalTimeFilter<T> extends AbstractLesserAttributeFilter<T, LocalTime> {

		public LocalTimeFilter(String name, String value, BiFunction<T, String, LocalTime> getter) {
			super(name, value, getter);
		}

		@Override
		protected LocalTime parse(String value) {
			return DateUtil.localTime(value);
		}
	}

	@EqualsAndHashCode(callSuper = true)
	private static class LocalDateTimeFilter<T> extends AbstractLesserAttributeFilter<T, LocalDateTime> {

		public LocalDateTimeFilter(String name, String value, BiFunction<T, String, LocalDateTime> getter) {
			super(name, value, getter);
		}
	
		@Override
		protected LocalDateTime parse(String value) {
			return DateUtil.localDateTime(value);
		}
	}

	@EqualsAndHashCode(callSuper = true)
	private static class OffsetDateTimeFilter<T> extends AbstractLesserAttributeFilter<T, OffsetDateTime> {

		public OffsetDateTimeFilter(String name, String value, BiFunction<T, String, OffsetDateTime> getter) {
			super(name, value, getter);
		}

		@Override
		protected OffsetDateTime parse(String value) {
			return DateUtil.offsetDateTime(value);
		}
	}

	@EqualsAndHashCode(callSuper = true)
	private static class ZonedDateTimeFilter<T> extends AbstractLesserAttributeFilter<T, ZonedDateTime> {

		public ZonedDateTimeFilter(String name, String value, BiFunction<T, String, ZonedDateTime> getter) {
			super(name, value, getter);
		}

		@Override
		protected ZonedDateTime parse(String value) {
			return DateUtil.zonedDateTime(value);
		}
	}
}

