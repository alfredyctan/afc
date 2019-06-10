package org.afc.filter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.afc.util.DateUtil;
import org.afc.util.MapUtil;

public class AttributeMock {

	public static Map<String, String> attributesLive() {
		return MapUtil.map(new HashMap<>(), 
			"STATUS", "LIVE",
			"AMOUNT", "1000000",
			"DATE", "2018-06-28",
			"LOCALDATE", "2018-06-28",
			"LOCALTIME", "10:15:30",
			"LOCALDATETIME", "2018-06-28T10:15:30",
			"OFFSETDATETIME", "2018-06-28T10:15:30+08:00",
			"ZONEDDATETIME", "2018-06-28T10:15:30+08:00[Asia/Hong_Kong]"
		);
	}

	public static Map<String, String> attributesClosed() {
		return MapUtil.map(new HashMap<>(), 
			"STATUS", "CLOSED",
			"AMOUNT", "500000",
			"DATE", "2018-06-04",
			"LOCALDATE", "2018-06-04",
			"LOCALTIME", "01:15:30",
			"LOCALDATETIME", "2018-06-04T01:15:30",
			"OFFSETDATETIME", "2018-06-04T01:15:30+08:00",
			"ZONEDDATETIME", "2018-06-04T01:15:30+08:00[Asia/Hong_Kong]"
		);
	}


	public static String getString(Map<String, String> attributes, String name) {
		return attributes.get(name);
	}

	public static BigDecimal getNumeric(Map<String, String> attributes, String name) {
		return new BigDecimal(attributes.get(name));
	}

	public static Date getDate(Map<String, String> attributes, String name) {
		return DateUtil.parseDate(attributes.get(name));
	}

	public static LocalDate getLocalDate(Map<String, String> attributes, String name) {
		return DateUtil.localDate(attributes.get(name));
	}

	public static LocalTime getLocalTime(Map<String, String> attributes, String name) {
		return DateUtil.localTime(attributes.get(name));
	}

	public static LocalDateTime getLocalDateTime(Map<String, String> attributes, String name) {
		return DateUtil.localDateTime(attributes.get(name));
	}

	public static OffsetDateTime getOffsetDateTime(Map<String, String> attributes, String name) {
		return DateUtil.offsetDateTime(attributes.get(name));
	}

	public static ZonedDateTime getZonedDateTime(Map<String, String> attributes, String name) {
		return DateUtil.zonedDateTime(attributes.get(name));
	}
}
