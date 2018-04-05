package org.afc.util;

public class NumericUtil {

	public static int parseInt(String s, String failureMsg) {
		try {
			return Integer.parseInt(s);
		} catch (Exception e) {
			throw new RuntimeException(failureMsg + ", root cause : " + e.getMessage());
		}
	}
	
	public static long parseLong(String s, String failureMsg) {
		try {
			return Long.parseLong(s);
		} catch (Exception e) {
			throw new RuntimeException(failureMsg + ", root cause : " + e.getMessage());
		}
	}

	public static float parseFloat(String s, String failureMsg) {
		try {
			return Float.parseFloat(s);
		} catch (Exception e) {
			throw new RuntimeException(failureMsg + ", root cause : " + e.getMessage());
		}
	}

	public static double parseDouble(String s, String failureMsg) {
		try {
			return Double.parseDouble(s);
		} catch (Exception e) {
			throw new RuntimeException(failureMsg + ", root cause : " + e.getMessage());
		}
	}
}
