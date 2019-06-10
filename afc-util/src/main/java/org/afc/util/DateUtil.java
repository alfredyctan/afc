package org.afc.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.afc.AFCException;

public class DateUtil {

	public static final class UTC {
		
		public static final TimeZone TIME_ZONE = TimeZone.getTimeZone("UTC");
		
		public static final ZoneId ZONE_ID = TIME_ZONE.toZoneId();
		
		public static final ZoneOffset ZONE_OFFSET = ZoneOffset.UTC;
	}
	
	private static final DatatypeFactory DATATYPE_FACTORY = datetypeFactory();

	public static final int DAY = Calendar.DATE;
		
	public static final int MONTH = Calendar.MONTH;

	public static final int YEAR = Calendar.YEAR;
	
	public static final int HOUR = Calendar.HOUR_OF_DAY;
	
	public static final int MINUTE = Calendar.MINUTE;
	
	public static final int SECOND = Calendar.SECOND;
	
	public static final int MILLISECOND = Calendar.MILLISECOND;
	
	private static final ThreadLocal<SimpleDateFormat> DATE_PATTERN = new ThreadLocal<SimpleDateFormat>() {
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd");
		}
	};

	private static final ThreadLocal<SimpleDateFormat> TIME_PATTERN = new ThreadLocal<SimpleDateFormat>() {
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("HH:mm:ss");
		}
	};
	
	private static final ThreadLocal<SimpleDateFormat> DATETIME_PATTERN = new ThreadLocal<SimpleDateFormat>() {
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}
	};

	private static final ThreadLocal<SimpleDateFormat> TIMESTAMP_PATTERN = new ThreadLocal<SimpleDateFormat>() {
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		}
	};

	private static final ThreadLocal<SimpleDateFormat> XML_DATETIME_PATTERN = new ThreadLocal<SimpleDateFormat>() {
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		}
	};

	private static DatatypeFactory datetypeFactory() {
		try {
			return DatatypeFactory.newInstance();
		} catch (DatatypeConfigurationException e) {
			return null;
		}
	}

	/**
	 * Format the incoming date to target format in the target time zone 
	 * @param date
	 * @param format
	 * @param timeZone
	 * @return
	 */
	private static String doFormat(Date date, ThreadLocal<SimpleDateFormat> format, TimeZone timeZone) {
		SimpleDateFormat dateFormat = format.get();
		dateFormat.setTimeZone(timeZone);
		return dateFormat.format(date);
	}

	/**
	 * Parse the incoming String to a Date object in the target time zone
	 * @param dateStr
	 * @param format
	 * @param timeZone
	 * @return
	 * @throws ParseException
	 */
	private static Date doParse(String dateStr, ThreadLocal<SimpleDateFormat> format, TimeZone timeZone) {
		SimpleDateFormat dateFormat = format.get();
		dateFormat.setTimeZone(timeZone);
		try {
			return dateFormat.parse(dateStr);
		} catch (ParseException e) {
			throw new AFCException("failed to parse : [" + dateStr + "], format : [" + dateFormat.toPattern() + ']', e);
		}
	}
	
	/**
	 * Format a Calendar in the format of ""yyyy-MM-dd"
	 * @param cal
	 * @return
	 */
	public static String formatDate(Calendar cal) {
		return formatDate(cal.getTime(), cal.getTimeZone());
	}
	
	
	/**
	 * Format a date in the format of "yyyy-MM-dd" with default time zone
	 * @param date
	 * @return
	 */
	public static String formatDate(Date date) {
		return doFormat(date, DATE_PATTERN, TimeZone.getDefault());
	}

	/**
	 * Format a date in the format of "yyyy-MM-dd" with target time zone
	 * @param date
	 * @param timeZone
	 * @return
	 */
	public static String formatDate(Date date, TimeZone timeZone) {
		return doFormat(date, DATE_PATTERN, timeZone);
	}

	/**
	 * Format a calendar to in the foramt of "yyyy-MM-dd HH:mm:ss" 
	 * @param cal
	 * @return
	 */
	public static String formatDatetime(Calendar cal){
		return formatDatetime(cal.getTime(), cal.getTimeZone());
	}
	
	/**
	 * Format date in format "yyyy-MM-dd HH:mm:ss" with default time zone
	 * @param date
	 * @return
	 */
	public static String formatDatetime(Date date){
		return doFormat(date, DATETIME_PATTERN, TimeZone.getDefault());
	}

	/**
	 * Format date in the format of "yyyy-MM-dd HH:mm:ss" with target time zone
	 * @param date
	 * @param timeZone
	 * @return
	 */
	public static String formatDatetime(Date date, TimeZone timeZone){
		return doFormat(date, DATETIME_PATTERN, timeZone);
	}

	/**
	 * Format a Calendar in the format of "HH:mm:ss" 
	 * @param cal
	 * @return
	 */
	public static String formatTime(Calendar cal){
		return formatTime(cal.getTime(), cal.getTimeZone());
	}

	/**
	 * Format a date in the format of "HH:mm:ss" with default time zone
	 * @param date
	 * @return
	 */
	public static String formatTime(Date date){
		return doFormat(date, TIME_PATTERN, TimeZone.getDefault());
	}

	/**
	 * Format a date in the format of "HH:mm:ss" with target time zone
	 * @param date
	 * @param timeZone
	 * @return
	 */
	public static String formatTime(Date date, TimeZone timeZone){
		return doFormat(date, TIME_PATTERN, timeZone);
	}

	/**
	 * Format timestamp in the format of "yyyy-MM-dd HH:mm:ss.SSS" with default time zone
	 * @param date
	 * @Return
	 */
	public static String formatTimeStamp(Calendar date) {
		return formatTimeStamp(date.getTime(), date.getTimeZone());
	}
	
	/**
	 * Format timestamp in the format of "yyyy-MM-dd HH:mm:ss.SSS" with default time zone
	 * @param date
	 * @Return
	 */
	public static String formatTimeStamp(Date date) {
		return doFormat(date, TIMESTAMP_PATTERN, TimeZone.getDefault());
	}

	/**
	 * Format timestamp in the format of "yyyy-MM-dd HH:mm:ss.SSS" with target time zone
	 * @param date
	 * @param timeZone
	 * @return
	 */
	public static String formatTimeStamp(Date date, TimeZone timeZone){
		return doFormat(date, TIMESTAMP_PATTERN, timeZone);
	}

	/**
	 * Format a date in the format of "yyyy-MM-dd" with default time zone
	 * @param date
	 * @return
	 */
	public static String formatUTCDate(Date date) {
		return doFormat(date, DATE_PATTERN, UTC.TIME_ZONE);
	}
	
	/**
	 * Format date in format "yyyy-MM-dd HH:mm:ss" with default time zone
	 * @param date
	 * @return
	 */
	public static String formatUTCDatetime(Date date){
		return doFormat(date, DATETIME_PATTERN, UTC.TIME_ZONE);
	}

	/**
	 * Format a date in the format of "HH:mm:ss" with default time zone
	 * @param date
	 * @return
	 */
	public static String formatUTCTime(Date date){
		return doFormat(date, TIME_PATTERN, UTC.TIME_ZONE);
	}

	/**
	 * Format timestamp in the format of "yyyy-MM-dd HH:mm:ss.SSS" with default time zone
	 * @param date
	 * @Return
	 */
	public static String formatUTCTimeStamp(Date date) {
		return doFormat(date, TIMESTAMP_PATTERN, UTC.TIME_ZONE);
	}
	
	/**
	 * Format a date in the format of "yyyy-MM-dd'T'HH:mm:ss" with default time zone
	 * @param date
	 * @return
	 */
	public static String formatXMLDatetime(Date date){
		return doFormat(date, XML_DATETIME_PATTERN, TimeZone.getDefault());
	}

	/**
	 * Format a date in the format of "yyyy-MM-dd'T'HH:mm:ss" with target time zone
	 * @param date
	 * @return
	 */
	public static String formatXMLDatetime(Date date, TimeZone timeZone){
		return doFormat(date, XML_DATETIME_PATTERN, timeZone);
	}

	/**
	 * parse a string to date in the default time zone 
	 * @param str
	 * @return
	 * @throws ParseException
	 */
	public static Date parseDate(String str) {
		return doParse(str, DATE_PATTERN, TimeZone.getDefault());
	}

	/**
	 * parse a string to date in the target time zone
	 * @param str
	 * @param timeZone
	 * @return
	 * @throws ParseException
	 */
	public static Date parseDate(String str, TimeZone timeZone) {
		return doParse(str, DATE_PATTERN, timeZone);
	}

	/**
	 * parse a string to datetime in defalut time zone
	 * @param str
	 * @return
	 * @throws ParseException
	 */
	public static Date parseDatetime(String str) {
		return doParse(str, DATETIME_PATTERN, TimeZone.getDefault());
	}
	
	/**
	 * parse a string to datetime in the target time zone
	 * @param str
	 * @param timeZone
	 * @return
	 * @throws ParseException
	 */
	public static Date parseDatetime(String str, TimeZone timeZone) {
		return doParse(str, DATETIME_PATTERN, timeZone);
	}

	/**
	 * Parse milliseconds to date
	 * @param str
	 * @return
	 */
	public static Date parseDateTimeInMillis(String str) {
		return new Date(Long.parseLong(str));
	}

	/**
	 * parse a String to a Date object with time only with default time zone
	 * @param str
	 * @return
	 * @throws ParseException
	 */
	public static Date parseTime(String str) {
		return doParse(str, TIME_PATTERN, TimeZone.getDefault());
	}

	/**
	 * parse a String to a Date object with time only with target time zone
	 * @param str
	 * @param timeZone
	 * @return
	 * @throws ParseException
	 */
	public static Date parseTime(String str, TimeZone timeZone) {
		return doParse(str, TIME_PATTERN, timeZone);
	}

	/**
	 * parse a string to datetime in defalut time zone
	 * @param str
	 * @return
	 * @throws ParseException
	 */
	public static Date parseTimestamp(String str) {
		return doParse(str, TIMESTAMP_PATTERN, TimeZone.getDefault());
	}

	/**
	 * parse a string to datetime in the target time zone
	 * @param str
	 * @param timeZone
	 * @return
	 * @throws ParseException
	 */
	public static Date parseTimestamp(String str, TimeZone timeZone) {
		return doParse(str, TIMESTAMP_PATTERN, timeZone);
	}

	/**
	 * parse a string to date in the default time zone 
	 * @param str
	 * @return
	 * @throws ParseException
	 */
	public static Date parseUTCDate(String str) {
		return doParse(str, DATE_PATTERN, UTC.TIME_ZONE);
	}

	/**
	 * parse a string to datetime in defalut time zone
	 * @param str
	 * @return
	 * @throws ParseException
	 */
	public static Date parseUTCDatetime(String str) {
		return doParse(str, DATETIME_PATTERN, UTC.TIME_ZONE);
	}
	
	/**
	 * parse a String to a Date object with time only with default time zone
	 * @param str
	 * @return
	 * @throws ParseException
	 */
	public static Date parseUTCTime(String str) {
		return doParse(str, TIME_PATTERN, UTC.TIME_ZONE);
	}

	/**
	 * parse a string to datetime in defalut time zone
	 * @param str
	 * @return
	 * @throws ParseException
	 */
	public static Date parseUTCTimestamp(String str) {
		return doParse(str, TIMESTAMP_PATTERN, UTC.TIME_ZONE);
	}

	/**
	 * parse a String to a Date object with time only with default time zone
	 * @param str
	 * @return
	 * @throws ParseException
	 */
	public static Date parseXMLDatetime(String str) {
		return doParse(str, XML_DATETIME_PATTERN, TimeZone.getDefault());
	}

	/**
	 * Add a date part specified. The date part can be DAY, MONTH, YEAR, 
	 * HOUR, MINUTE, SECOND, MILLISECOND defined in DateUtil.
	 * 
	 * @param datePart - the part of date
	 * @param date - the date to add
	 * @param amount - the amount of part to add
	 * @return the added Date object
	 */
	public static Date dateAdd(int datePart, Date date, int amount) {
		Calendar cal = toCalendar(date);
		cal.add(datePart, amount);
		return cal.getTime();
	}

	/**
	 * To retrieve the date part in a date. The date part can be DAY, MONTH, YEAR, 
	 * HOUR, MINUTE, SECOND, MILLISECOND defined in DateUtil.
	 * 
	 * @param datePart - the part of date
	 * @param date - the date to retrieve 
	 * @return the date part amount
	 */
	public static int datePart(int datePart, Date date) {
		Calendar cal = toCalendar(date);
		return cal.get(datePart);
	}
	
	/**
	 * Set the date part in Date object.
	 * 
	 * @param datePart - the part of date
	 * @param date - the date to set
	 * @param amount - the amount of part to set
	 * @return the set Date object
	 */
	public static Date dateSet(int datePart, Date date, int amount) {
		Calendar cal = toCalendar(date);
		cal.set(datePart, amount);
		return cal.getTime();
	}

	/**
	 * Compare timestamp in long format
	 * 
	 * @param date1 - the date 1
	 * @param date2 - the date 2
	 * @return true if date1 later than date2, else false
	 */
	public static boolean isAfter(Calendar date1, Calendar date2) {
		return isAfter(date1.getTime(), date2.getTime());
	}

	/**
	 * Compare timestamp in long format
	 * 
	 * @param date1 - the date 1
	 * @param date2 - the date 2
	 * @return true if date1 later than date2, else false
	 */
	public static boolean isAfter(Date date1, Date date2) {
		return isAfter(date1.getTime(), date2.getTime());
	}

	/**
	 * Compare timestamp in long format
	 * 
	 * @param date1 - the date 1
	 * @param date2 - the date 2
	 * @return true if date1 later than date2, else false
	 */
	public static boolean isAfter(long date1, long date2) {
		return (date1 > date2);
	}
	
	/**
	 * Compare timestamp in long format
	 * 
	 * @param date1 - the date 1
	 * @param date2 - the date 2
	 * @return true if date1 earlier than date2, else false
	 */
	public static boolean isBefore(Calendar date1, Calendar date2) {
		return isBefore(date1.getTime(), date2.getTime());
	}
	
	/**
	 * Compare timestamp in long format
	 * 
	 * @param date1 - the date 1
	 * @param date2 - the date 2
	 * @return true if date1 earlier than date2, else false
	 */
	public static boolean isBefore(Date date1, Date date2) {
		return isBefore(date1.getTime(), date2.getTime());
	}
	
	/**
	 * Compare timestamp in long format
	 * 
	 * @param date1 - the date 1
	 * @param date2 - the date 2
	 * @return true if date1 earlier than date2, else false
	 */
	public static boolean isBefore(long date1, long date2) {
		return (date1 < date2);
	}


	/**
	 * Set the time component in Date object.
	 * 
	 * @param date - the date to set
	 * @param hh - hour
	 * @param mm - minute
	 * @param ss - second
	 * @param ms - millisecond
	 * @return the set Date
	 */
	public static Date setTime(Date date, int hh, int mm, int ss, int ms) {
		Calendar cal = toCalendar(date);
		cal.set(HOUR, hh);
		cal.set(MINUTE, mm);
		cal.set(SECOND, ss);
		cal.set(MILLISECOND, ms);
		return cal.getTime();
	}
	
	/**
	 * Convert date to calendar
	 * @param dateTime
	 * @return
	 */
	public static Calendar toCalendar(Date dateTime) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dateTime);
		return cal;
	}

	/**
	 * Convert milliseconds to calendar
	 * @param milliseconds
	 * @return
	 */
	public static Calendar toCalendar(long milliseconds) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(milliseconds);
		return cal;
	}

	/**
	 * Reset the time component in Date object, so that date object becomes 
	 * a midnight day
	 * 
	 * @param date - the date to reset
	 * @return the reset Date
	 */
	public static Date toMidNight(Date date) {
		return setTime(date, 0, 0, 0, 0);
	}

	/**
	 * Convert calendar to milliseconds
	 * @param cal
	 * @return
	 */
	public static long toMilliseconds(Calendar cal){
		return cal.getTimeInMillis();
	}

	/**
	 * Convert date to milliseconds
	 * @param date
	 * @return
	 */
	public static long toMilliseconds(Date date){
		return date.getTime();
	}
	
	public static Date date(LocalDate localDate) {
		return date(localDate, ZoneId.systemDefault());
	}

	public static Date date(LocalDate localDate, ZoneId zoneId) {
		return date(localDate.atStartOfDay(zoneId));
	}
	
	public static Date date(LocalDateTime localDateTime) {
		return date(localDateTime, ZoneId.systemDefault());
	}

	public static Date date(LocalDateTime localDateTime, ZoneId zoneId) {
		return date(localDateTime.atZone(zoneId));
	}

	public static Date date(LocalTime localTime) {
		return date(localTime, ZoneId.systemDefault());
	}
	
	public static Date date(LocalTime localTime, ZoneId zoneId) {
		return date(localTime.atDate(LocalDate.now()), zoneId);
	}

	public static Date date(OffsetDateTime offsetDateTime) {
		return Date.from(offsetDateTime.toInstant());
	}

	public static Date date(ZonedDateTime zonedDateTime) {
		return Date.from(zonedDateTime.toInstant());
	}
	
	public static LocalDate localDate(Date date) {
		return localDate(date, ZoneId.systemDefault());
	}

	public static LocalDate localDate(Date date, ZoneId zoneId) {
		return date.toInstant().atZone(zoneId).toLocalDate();
	}

	public static LocalDate localDate(String date) {
		return (date != null) ? LocalDate.parse(date) : null;
	}

	public static LocalDate localDate(XMLGregorianCalendar date) {
		return (date != null) ? date.toGregorianCalendar().toZonedDateTime().toLocalDate() : null;
	}


	public static LocalDate localDate(XMLGregorianCalendar date, ZoneId zoneId) {
		return (date != null) ? date.toGregorianCalendar().toZonedDateTime().withZoneSameInstant(zoneId).toLocalDate() : null;
	}

	public static LocalTime localTime(Date date) {
		return localTime(date, ZoneId.systemDefault());
	}
	
	public static LocalTime localTime(Date date, ZoneId zoneId) {
		return date.toInstant().atZone(zoneId).toLocalTime();
	}
	
	public static LocalTime localTime(String time) {
		return (time != null) ? LocalTime.parse(time) : null;
	}

	public static LocalTime localTime(XMLGregorianCalendar date) {
		return (date != null) ? date.toGregorianCalendar().toZonedDateTime().toLocalTime() : null;
	}

	public static LocalTime localTime(XMLGregorianCalendar date, ZoneId zoneId) {
		return (date != null) ? date.toGregorianCalendar().toZonedDateTime().withZoneSameInstant(zoneId).toLocalTime() : null;
	}

	public static LocalDateTime localDateTime(Date date) {
		return localDateTime(date, ZoneId.systemDefault());
	}
	
	public static LocalDateTime localDateTime(Date date, ZoneId zoneId) {
		return date.toInstant().atZone(zoneId).toLocalDateTime();
	}
	public static LocalDateTime localDateTime(OffsetDateTime dateTime) {
		return localDateTime(dateTime, ZoneId.systemDefault());
	}
	
	public static LocalDateTime localDateTime(OffsetDateTime dateTime, ZoneId zoneId) {
		return dateTime.atZoneSameInstant(zoneId).toLocalDateTime();
	}

	public static LocalDateTime localDateTime(ZonedDateTime dateTime) {
		return (dateTime != null) ? dateTime.toLocalDateTime() : null;
	}
	
	public static LocalDateTime localDateTime(String dateTime) {
		return (dateTime != null) ? LocalDateTime.parse(dateTime) : null;
	}

	public static LocalDateTime localDateTime(XMLGregorianCalendar date) {
		return (date != null) ? date.toGregorianCalendar().toZonedDateTime().toLocalDateTime() : null;
	}
	
	public static LocalDateTime localDateTime(XMLGregorianCalendar date, ZoneId zoneId) {
		return (date != null) ? date.toGregorianCalendar().toZonedDateTime().withZoneSameInstant(zoneId).toLocalDateTime() : null;
	}

	public static OffsetDateTime offsetDateTime(Date date) {
		return offsetDateTime(date, ZoneId.systemDefault());
	}

	public static OffsetDateTime offsetDateTime(Date date, ZoneId zoneId) {
		return OffsetDateTime.ofInstant(date.toInstant(), zoneId);
	}

	public static OffsetDateTime offsetDateTime(LocalDateTime dateTime) {
		return offsetDateTime(dateTime, ZoneId.systemDefault());
	}

	public static OffsetDateTime offsetDateTime(LocalDateTime dateTime, ZoneId zoneId) {
		return dateTime.atOffset(zoneId.getRules().getOffset(ClockUtil.instant()));
	}

	public static OffsetDateTime offsetDateTime(ZonedDateTime dateTime) {
		return (dateTime != null) ? dateTime.toOffsetDateTime() : null;
	}

	public static OffsetDateTime offsetDateTime(String dateTime) {
		return (dateTime != null) ? OffsetDateTime.parse(dateTime) : null;
	}

	public static OffsetDateTime offsetDateTime(XMLGregorianCalendar date) {
		return (date != null) ? date.toGregorianCalendar().toZonedDateTime().toOffsetDateTime() : null;
	}

	public static OffsetDateTime offsetDateTime(XMLGregorianCalendar date, ZoneId zoneId) {
		return (date != null) ? date.toGregorianCalendar().toZonedDateTime().withZoneSameInstant(zoneId).toOffsetDateTime() : null;
	}

	public static ZonedDateTime zonedDateTime(Date date) {
		return zonedDateTime(date, ZoneId.systemDefault());
	}

	public static ZonedDateTime zonedDateTime(Date date, ZoneId zoneId) {
		return ZonedDateTime.ofInstant(date.toInstant(), zoneId);
	}

	public static ZonedDateTime zonedDateTime(LocalDateTime dateTime) {
		return zonedDateTime(dateTime, ZoneId.systemDefault());
	}

	public static ZonedDateTime zonedDateTime(LocalDateTime dateTime, ZoneId zoneId) {
		return dateTime.atZone(zoneId);
	}

	public static ZonedDateTime zonedDateTime(OffsetDateTime dateTime) {
		return zonedDateTime(dateTime, ZoneId.systemDefault());
	}

	public static ZonedDateTime zonedDateTime(OffsetDateTime dateTime, ZoneId zoneId) {
		return dateTime.atZoneSameInstant(zoneId);
	}
	
	public static ZonedDateTime zonedDateTime(String dateTime) {
		return (dateTime != null) ? ZonedDateTime.parse(dateTime) : null;
	}

	public static ZonedDateTime zonedDateTime(XMLGregorianCalendar date) {
		return (date != null) ? date.toGregorianCalendar().toZonedDateTime() : null;
	}

	public static ZonedDateTime zonedDateTime(XMLGregorianCalendar date, ZoneId zoneId) {
		return (date != null) ? date.toGregorianCalendar().toZonedDateTime().withZoneSameInstant(zoneId) : null;
	}

	public static XMLGregorianCalendar xmlGregorianCalendar(LocalDate localDate) {
		XMLGregorianCalendar cal = xmlGregorianCalendar(localDate, ZoneId.systemDefault());
		cal.setTimezone(DatatypeConstants.FIELD_UNDEFINED); //localDate has no timezone information
		return cal;
	}

	public static XMLGregorianCalendar xmlGregorianCalendar(LocalDate localDate, ZoneId zoneId) {
		return DATATYPE_FACTORY.newXMLGregorianCalendar(
			GregorianCalendar.from(localDate.atStartOfDay(zoneId))
		);
	}

	public static XMLGregorianCalendar xmlGregorianCalendar(LocalDateTime localDateTime) {
		XMLGregorianCalendar cal = xmlGregorianCalendar(localDateTime, ZoneId.systemDefault());
		cal.setTimezone(DatatypeConstants.FIELD_UNDEFINED); //localDateTime has no timezone information
		return cal;
	}

	public static XMLGregorianCalendar xmlGregorianCalendar(LocalDateTime localDateTime, ZoneId zoneId) {
		return DATATYPE_FACTORY.newXMLGregorianCalendar(
			GregorianCalendar.from(localDateTime.atZone(zoneId))
		);
	}

	public static XMLGregorianCalendar xmlGregorianCalendar(LocalTime localTime) {
		XMLGregorianCalendar cal = xmlGregorianCalendar(localTime, ZoneId.systemDefault());
		cal.setTimezone(DatatypeConstants.FIELD_UNDEFINED); //localTime has no timezone information
		return cal;
	}
	
	public static XMLGregorianCalendar xmlGregorianCalendar(LocalTime localTime, ZoneId zoneId) {
		XMLGregorianCalendar cal = DATATYPE_FACTORY.newXMLGregorianCalendar(
			GregorianCalendar.from(localTime.atDate(ClockUtil.localDate()).atZone(zoneId))
		);
		cal.setYear(DatatypeConstants.FIELD_UNDEFINED);
		cal.setMonth(DatatypeConstants.FIELD_UNDEFINED);
		cal.setDay(DatatypeConstants.FIELD_UNDEFINED);
		return cal;
	}

	public static XMLGregorianCalendar xmlGregorianCalendar(OffsetDateTime offsetDateTime) {
		return DATATYPE_FACTORY.newXMLGregorianCalendar(
			GregorianCalendar.from(offsetDateTime.toZonedDateTime())
		);
	}

	public static XMLGregorianCalendar xmlGregorianCalendar(ZonedDateTime zonedDateTime) {
		return DATATYPE_FACTORY.newXMLGregorianCalendar(
			GregorianCalendar.from(zonedDateTime)
		);
	}
}