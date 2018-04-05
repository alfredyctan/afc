package org.afc.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {

	public static final int DAY = Calendar.DATE;
	public static final int MONTH = Calendar.MONTH;
	public static final int YEAR = Calendar.YEAR;
	public static final int HOUR = Calendar.HOUR_OF_DAY;
	public static final int MINUTE = Calendar.MINUTE;
	public static final int SECOND = Calendar.SECOND;
	public static final int MILLISECOND = Calendar.MILLISECOND;

	private static final TimeZone TIMEZONE_UTC = TimeZone.getTimeZone("UTC");

	private static final String DATE_PATTERN = "yyyy-MM-dd";
	private static final String TIME_PATTERN = "HH:mm:ss";
	private static final String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";	
	private static final String TIMESTAMP_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";
	
	private static final String XML_DATETIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";	
	
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
	 * Format timestamp in the format of "yyyy-MM-dd HH:mm:ss.SSS" with default time zone
	 * @param date
	 * @Return
	 */
	public static String formatTimeStamp(Calendar date) {
		return formatTimeStamp(date.getTime(), date.getTimeZone());
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
	 * Format a calendar to in the foramt of "yyyy-MM-dd HH:mm:ss" 
	 * @param cal
	 * @return
	 */
	public static String formatDatetime(Calendar cal){
		return formatDatetime(cal.getTime(), cal.getTimeZone());
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
	 * Format a date in target format with default time zone 
	 * @param date
	 * @param format
	 * @return
	 */
	public static String formatDate(Date date, String format) {
		return doFormat(date, format, TimeZone.getDefault());
	}

	/**
	 * Format a date in target format and target time zone
	 * @param date
	 * @param format
	 * @param timeZone
	 * @return
	 */
	public static String formatDate(Date date, String format, TimeZone timeZone) {
		return doFormat(date, format, timeZone);
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
	 * Format a Calendar in the format of "HH:mm:ss" 
	 * @param cal
	 * @return
	 */
	public static String formatTime(Calendar cal){
		return formatTime(cal.getTime(), cal.getTimeZone());
	}

	/**
	 * Format timestamp in the format of "yyyy-MM-dd HH:mm:ss.SSS" with default time zone
	 * @param date
	 * @Return
	 */
	public static String formatUTCTimeStamp(Date date) {
		return doFormat(date, TIMESTAMP_PATTERN, TIMEZONE_UTC);
	}
	
	/**
	 * Format date in format "yyyy-MM-dd HH:mm:ss" with default time zone
	 * @param date
	 * @return
	 */
	public static String formatUTCDatetime(Date date){
		return doFormat(date, DATETIME_PATTERN, TIMEZONE_UTC);
	}
	
	
	/**
	 * Format a date in the format of "yyyy-MM-dd" with default time zone
	 * @param date
	 * @return
	 */
	public static String formatUTCDate(Date date) {
		return doFormat(date, DATE_PATTERN, TIMEZONE_UTC);
	}

	/**
	 * Format a date in the format of "HH:mm:ss" with default time zone
	 * @param date
	 * @return
	 */
	public static String formatUTCTime(Date date){
		return doFormat(date, TIME_PATTERN, TIMEZONE_UTC);
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
	 * parse a string to datetime in defalut time zone
	 * @param str
	 * @return
	 * @throws ParseException
	 */
	public static Date parseDatetime(String str) throws ParseException {
		return doParse(str, DATETIME_PATTERN, TimeZone.getDefault());
	}

	/**
	 * parse a string to datetime in the target time zone
	 * @param str
	 * @param timeZone
	 * @return
	 * @throws ParseException
	 */
	public static Date parseDatetime(String str, TimeZone timeZone) throws ParseException {
		return doParse(str, DATETIME_PATTERN, timeZone);
	}

	/**
	 * parse a string to datetime in defalut time zone
	 * @param str
	 * @return
	 * @throws ParseException
	 */
	public static Date parseTimestamp(String str) throws ParseException {
		return doParse(str, TIMESTAMP_PATTERN, TimeZone.getDefault());
	}

	/**
	 * parse a string to datetime in the target time zone
	 * @param str
	 * @param timeZone
	 * @return
	 * @throws ParseException
	 */
	public static Date parseTimestamp(String str, TimeZone timeZone) throws ParseException {
		return doParse(str, TIMESTAMP_PATTERN, timeZone);
	}

	/**
	 * parse a string to date in the default time zone 
	 * @param str
	 * @return
	 * @throws ParseException
	 */
	public static Date parseDate(String str) throws ParseException {
		return doParse(str, DATE_PATTERN, TimeZone.getDefault());
	}
	
	/**
	 * parse a string to date in the target time zone
	 * @param str
	 * @param timeZone
	 * @return
	 * @throws ParseException
	 */
	public static Date parseDate(String str, TimeZone timeZone) throws ParseException {
		return doParse(str, DATE_PATTERN, timeZone);
	}

	/**
	 * parse a String to a Date object with time only with default time zone
	 * @param str
	 * @return
	 * @throws ParseException
	 */
	public static Date parseTime(String str) throws ParseException {
		return doParse(str, TIME_PATTERN, TimeZone.getDefault());
	}

	/**
	 * parse a String to a Date object with time only with target time zone
	 * @param str
	 * @param timeZone
	 * @return
	 * @throws ParseException
	 */
	public static Date parseTime(String str, TimeZone timeZone) throws ParseException {
		return doParse(str, TIME_PATTERN, timeZone);
	}
	
	/**
	 * parse a string to datetime in defalut time zone
	 * @param str
	 * @return
	 * @throws ParseException
	 */
	public static Date parseUTCDatetime(String str) throws ParseException {
		return doParse(str, DATETIME_PATTERN, TIMEZONE_UTC);
	}

	/**
	 * parse a string to datetime in defalut time zone
	 * @param str
	 * @return
	 * @throws ParseException
	 */
	public static Date parseUTCTimestamp(String str) throws ParseException {
		return doParse(str, TIMESTAMP_PATTERN, TIMEZONE_UTC);
	}

	/**
	 * parse a string to date in the default time zone 
	 * @param str
	 * @return
	 * @throws ParseException
	 */
	public static Date parseUTCDate(String str) throws ParseException {
		return doParse(str, DATE_PATTERN, TIMEZONE_UTC);
	}
	
	/**
	 * parse a String to a Date object with time only with default time zone
	 * @param str
	 * @return
	 * @throws ParseException
	 */
	public static Date parseUTCTime(String str) throws ParseException {
		return doParse(str, TIME_PATTERN, TIMEZONE_UTC);
	}

	/**
	 * parse a String to a Date object with time only with default time zone
	 * @param str
	 * @return
	 * @throws ParseException
	 */
	public static Date parseXMLDatetime(String str) throws ParseException {
		return doParse(str, XML_DATETIME_PATTERN, TimeZone.getDefault());
	}

	/**
	 * Format the incoming date to target format in the target time zone 
	 * @param date
	 * @param format
	 * @param timeZone
	 * @return
	 */
	private static String doFormat(Date date, String format, TimeZone timeZone) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(format) ;
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
	private static Date doParse(String dateStr, String format, TimeZone timeZone) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat(format) ;
		dateFormat.setTimeZone(timeZone);
		return dateFormat.parse(dateStr);
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
	 * Convert date to milliseconds
	 * @param date
	 * @return
	 */
	public static long toMilliseconds(Date date){
		return date.getTime();
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
	public static boolean isBefore(Date date1, Date date2) {
		return isBefore(date1.getTime(), date2.getTime());
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
	 * @return true if date1 later than date2, else false
	 */
	public static boolean isAfter(Calendar date1, Calendar date2) {
		return isAfter(date1.getTime(), date2.getTime());
	}
}
