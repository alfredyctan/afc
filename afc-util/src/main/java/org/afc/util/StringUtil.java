package org.afc.util;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.helpers.MessageFormatter;

/**
 * 
 * This class provide basic utility function to manipulate String
 *
 */
public class StringUtil {

	private static final char DELIMITER = '/';

	private static final ThreadLocal<Field> PATTERN_NAMED_GROUPS = ThreadLocal.withInitial(() -> {
		try {
			Field field = Pattern.class.getDeclaredField("namedGroups");
			field.setAccessible(true);
			return field;
		} catch (NoSuchFieldException | SecurityException e) {
			return null;
		}
	});

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

	public static String padding(char append, int length) {
		char[] appending = new char[length];
		Arrays.fill(appending, append);
		return String.valueOf(appending);
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

	public static String leftCut(String s, int length) {
		return s.substring(length);
	}
	
	public static String rightCut(String s, int length) {
		return s.substring(0, s.length() - length);
	}

	public static String edgeCut(String s, int l, int r) {
		return s.substring(l, s.length() - r);
	}
	
	/**
	 * Check whether the given string is non-null or non-empty
	 * 
	 * eg. null : false
	 * "" : false
	 * "x" : true
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
	 * "" : true
	 * "x" : false
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
	 * s1 / s2 : result
	 * ---------- ------
	 * null/null : true
	 * null/"" : false
	 * ""/null : false
	 * ""/"" : true
	 * "A"/"A" : true
	 * "A"/"B" : false
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
	 * s1 / s2 : result
	 * ---------- ------
	 * null/null : false
	 * null/"" : true
	 * ""/null : true
	 * ""/"" : false
	 * "A"/"A" : false
	 * "A"/"B" : true
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
		if (value instanceof Collection<?> && ((Collection<?>) value).size() == 0) {
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

	public static Map<String, String> namedMatch(String regex, CharSequence seq) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(seq);
		if (!matcher.find()) {
			return null;
		} else {
			Map<String, String> map = new HashMap<>();
			try {
				Map<String, Integer> index = (Map<String, Integer>) PATTERN_NAMED_GROUPS.get().get(matcher.pattern());
				for (String name : index.keySet()) {
					map.put(name, matcher.group(name));
				}
			} catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
			}
			return map;
		}
	}
}
