package org.afc.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.afc.AFCException;

public class NumericUtil {

	public static final BigDecimal HUNDRED = new BigDecimal(100);

	public static BigDecimal bigDecimal(String val) {
		return (val == null) ? null : new BigDecimal(val);
	}
	
	public static int parseInt(String s, String failureMsg) {
		try {
			return Integer.parseInt(s);
		} catch (Exception e) {
			throw new AFCException(failureMsg + ", root cause : " + e.getMessage());
		}
	}
	
	public static long parseLong(String s, String failureMsg) {
		try {
			return Long.parseLong(s);
		} catch (Exception e) {
			throw new AFCException(failureMsg + ", root cause : " + e.getMessage());
		}
	}

	public static float parseFloat(String s, String failureMsg) {
		try {
			return Float.parseFloat(s);
		} catch (Exception e) {
			throw new AFCException(failureMsg + ", root cause : " + e.getMessage());
		}
	}

	public static double parseDouble(String s, String failureMsg) {
		try {
			return Double.parseDouble(s);
		} catch (Exception e) {
			throw new AFCException(failureMsg + ", root cause : " + e.getMessage());
		}
	}
	
	public static BigDecimal fromPercentage(BigDecimal val) {
		return (val == null) ? null : val.divide(HUNDRED);
	}
	
	public static BigDecimal toPercentage(BigDecimal val) {
		return (val == null) ? null : val.multiply(HUNDRED);
	}
	
	public static BigDecimal random(double from, double to, int scale) {
		return new BigDecimal(Math.random() * (to - from) + from).setScale(scale, RoundingMode.HALF_UP);
	}
}