package org.afc.util;

import java.util.Arrays;
import java.util.Collection;

import org.slf4j.helpers.MessageFormatter;

/**
 * 
 * This class provide basic utility function to manipulate String 
 *
 */
public class StringUtil {

	private static final char DELIMITER = '/';
	
	public static String createKey(Object... tokens) {
		return createKey(DELIMITER, tokens);
	}

	public static String createKey(char delimiter, Object... tokens) {
		StringBuilder builder = new StringBuilder();
		builder.append(tokens[0]);
		for (int i = 1; i < tokens.length; i++) {
			builder.append(delimiter);
			builder.append(tokens[i]);
		}
		return builder.toString();
	}
	
	public static String format(String pattern, Object arg1) {
		return MessageFormatter.format(pattern, arg1).getMessage();
	}

	public static String format(String pattern, Object arg1, Object arg2) {
		return MessageFormatter.format(pattern, arg1, arg2).getMessage();
	}

	public static String format(String pattern, Object... args) {
		return MessageFormatter.arrayFormat(pattern, args).getMessage();
	}
	
	public static String fixLengthAppend(String s, char append, int length) {
		char[] appending = new char[length];
		Arrays.fill(appending, append);
		return left(s + String.valueOf(appending), length);
	}
	
	public static String fixLengthInsert(char insert, String s, int length) {
		char[] appending = new char[length];
		Arrays.fill(appending, insert);
		return right(String.valueOf(appending) + s, length);
	}
	
	public static String left(String s, int length) {
		return s.substring(0, length);
	}

	public static String right(String s, int length) {
		return s.substring(s.length() - length);
	}
	
	/**
	 * Check whether the given string is non-null or non-empty
	 * 
	 * eg. null : false
	 *     ""   : false
	 *     "x"  : true
	 * 
	 * @param string
	 * @return
	 */
	public static boolean hasValue(String string) {
		return (string != null && string.length() != 0); 
	}

	/**
	 * Check whether the given string is null or empty
	 * 
	 * eg. null : true
	 *     ""   : true
	 *     "x"  : false
	 * 
	 * @param string
	 * @return
	 */	
	public static boolean hasNoValue(String string) {
		return (string == null || string.length() == 0);
	}
	
	/**
	 * Check whether s1 and s2 has same value (including null)
	 * 
	 * eg.
	 * s1 / s2   : result
	 * ----------  ------
	 * null/null : true
	 * null/""   : false
	 * ""/null   : false
	 * ""/""     : true
	 * "A"/"A"   : true
	 * "A"/"B"   : false
	 * 
	 * @param s1
	 * @param s2
	 * @return
	 */
	public static boolean isSameValue(String s1, String s2) {
		if (s1 == null && s2 == null) {
			return true;
		}
		if (s1 != null && s2 != null) {
			return s1.equals(s2);
		}
		return false;
	}

	/**
	 * Check whether s1 and s2 has same value (including null)
	 * 
	 * eg.
	 * s1 / s2   : result
	 * ----------  ------
	 * null/null : false
	 * null/""   : true
	 * ""/null   : true
	 * ""/""     : false
	 * "A"/"A"   : false
	 * "A"/"B"   : true
	 * 
	 * @param s1
	 * @param s2
	 * @return
	 */
	public static boolean isNotSameValue(String s1, String s2) {
		if (s1 == null && s2 == null) {
			return false;
		}
		if (s1 != null && s2 != null) {
			return !s1.equals(s2);
		}
		return true;
	}

	public static StringBuilder startToString(StringBuilder builder) {
		builder.append('[');
		return builder;
	}

	public static StringBuilder buildToString(StringBuilder builder, String name, Object value) {
		if (value == null) {
			return builder;
		}
		if (value instanceof Collection<?> && ((Collection<?>)value).size() == 0) {
			return builder;
		}
		builder.append(name).append('=').append(value).append(", ");
		return builder;
	}

	public static StringBuilder endToString(StringBuilder builder) {
		int l = builder.length();
		if (l > 2) {
			builder.delete(l - 2, l);
		}
		builder.append(']');
		return builder;
	}
}
