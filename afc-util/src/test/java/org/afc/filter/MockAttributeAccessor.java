package org.afc.filter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;

import org.afc.util.DateUtil;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class MockAttributeAccessor implements AttributeAccessor<Map<String, String>>{

	public String getString(Map<String, String> attributes, String name) {
		return attributes.get(name);
	}

	public BigDecimal getNumeric(Map<String, String> attributes, String name) {
		return new BigDecimal(attributes.get(name));
	}

	public Date getDate(Map<String, String> attributes, String name) {
		return DateUtil.parseDate(attributes.get(name));
	}

	public LocalDate getLocalDate(Map<String, String> attributes, String name) {
		return DateUtil.localDate(attributes.get(name));
	}

	public LocalTime getLocalTime(Map<String, String> attributes, String name) {
		return DateUtil.localTime(attributes.get(name));
	}

	public LocalDateTime getLocalDateTime(Map<String, String> attributes, String name) {
		return DateUtil.localDateTime(attributes.get(name));
	}

	public OffsetDateTime getOffsetDateTime(Map<String, String> attributes, String name) {
		return DateUtil.offsetDateTime(attributes.get(name));
	}

	public ZonedDateTime getZonedDateTime(Map<String, String> attributes, String name) {
		return DateUtil.zonedDateTime(attributes.get(name));
	}

}
