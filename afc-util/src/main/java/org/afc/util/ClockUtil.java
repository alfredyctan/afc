package org.afc.util;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.TimeZone;

public class ClockUtil {

	private static Clock clock;
	static {
		clock = Clock.systemUTC();
	}
	
	public static void setClock(Clock clock) {
		ClockUtil.clock = clock;
	}

	public static Clock clock() {
		return ClockUtil.clock;
	}
	
	public static Instant instant() { 
		return clock.instant();
	}

	public static Date date() {
		return new Date(clock.millis());
	}
	
	public static long currentTimeMillis() {
		return clock.millis();
	}

	public static LocalDate localDate() {
		return LocalDate.now(clock);
	}

	public static LocalDateTime localDateTime() {
		return LocalDateTime.now(clock);
	}
	
	public static LocalTime localTime() {
		return LocalTime.now(clock);
	}

	public static ZonedDateTime zonedDateTime() {
		return ZonedDateTime.now(clock);
	}
	
	public static ZonedDateTime zonedDateTime(ZoneId zoneId) {
		return ZonedDateTime.now(clock).withZoneSameInstant(zoneId);
	}

	public static OffsetDateTime offsetDateTime() {
		return OffsetDateTime.now(clock);
	}
	
	public static OffsetDateTime offsetDateTime(ZoneOffset zoneOffset) {
		return OffsetDateTime.now(clock).withOffsetSameInstant(zoneOffset);
	}
	
	public static ZoneId defaultZoneId() {
		return ZoneId.systemDefault();
	}

	public static ZoneId utcZoneId() {
		return ZoneId.of("UTC");
	}

	public static void utcTimeZone() {
		timeZone(utcZoneId());
	}

	public static void timeZone(ZoneId id) {
		TimeZone.setDefault(TimeZone.getTimeZone(id));
	}
	
	/**
	 * Fix the clock returned
	 *  
	 * time : time string parse
	 * eg. "2007-12-03T10:15:30.00Z"
	 */
	public static void fixedClock(String time) {
		ClockUtil.clock = Clock.fixed(Instant.parse(time), ZoneOffset.UTC);
	}
}
