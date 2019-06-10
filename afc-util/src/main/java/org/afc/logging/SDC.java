package org.afc.logging;

import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.MDC;

import org.afc.util.StringUtil;


/**
 * Single Diagnostic Context, which is an Util class to simplify the
 * usage of MDC (Multi Diagnostic Contexts) with a pre-defined context "CTX"
 * in Multi-Threaded environment.
 *
 * <param name="ConversionPattern"
 * value="%d{yyyy-MM-dd HH:mm:ss.SSS} [%-5p][%t|%c{1}][%X{SDC}] : %m%n" />
 *
 * @author atyc
 *
 */
public class SDC {

	private static final AtomicInteger atomicSeed = new AtomicInteger();

	private static final char DEL = '.';

	public static final String SDC = "SDC";

	private SDC() {}

	public static String set(String message) {
		MDC.put(SDC, message);
		return message;
	}

	public static String set(int i) {
		return set(String.valueOf(i));
	}

	public static String set(long l) {
		return set(String.valueOf(l));
	}

	public static String set(char c) {
		return set(String.valueOf(c));
	}

	public static String set(short s) {
		return set(String.valueOf(s));
	}

	public static String set(double d) {
		return set(String.valueOf(d));
	}

	public static String set(Object object) {
		return set(hash(object));
	}

	public static String peek() {
		return MDC.get(SDC);
	}

	public static String remove() {
		String context = MDC.get(SDC);
		MDC.remove(SDC);
		return context;
	}

	public static String push(int i) {
		return push(String.valueOf(i));
	}

	public static String push(long l) {
		return push(String.valueOf(l));
	}

	public static String push(char c) {
		return push(String.valueOf(c));
	}

	public static String push(short s) {
		return push(String.valueOf(s));
	}

	public static String push(double d) {
		return set(String.valueOf(d));
	}

	public static String push(Object object) {
		return push(hash(object));
	}

	public static String push(String s) {
		return push(s, DEL);
	}

	public static String push(String s, char del) {
		String context = MDC.get(SDC) + del + s;
		MDC.put(SDC, context);
		return context;
	}

	public static String pop() {
		return pop(DEL);
	}

	public static String pop(char del) {
		String context = MDC.get(SDC);
		int i = context.lastIndexOf(del);
		if (i > 0) {
			MDC.put(SDC, context.substring(0, i));
			return context.substring(i + 1);
		} else {
			return "";
		}
	}

	public static String auto() {
		return auto(new Object());
	}

	public static String auto(Object object) {
		String sdc = peek();
		return sdc != null ? sdc : set(object);
	}

	public static String hash(Object obj) {
		return StringUtil.right(Long.toString((System.currentTimeMillis() + obj.hashCode() + atomicSeed.getAndIncrement()), 36).toUpperCase(), 5);
	}

}
