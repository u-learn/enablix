package com.enablix.commons.util;

public class StringUtil {
	
	public static boolean isEmpty(String str) {
		return str == null || str.isEmpty();
	}
	
	public static boolean hasText(String str) {
		return str != null && !str.trim().isEmpty();
	}

	public static String stringValue(Object obj) {
		if (obj != null) {
			return String.valueOf(obj);
		}
		return null;
	}
	
	public static boolean isNotStringAndEmpty(Object obj) {
		return !((obj instanceof String) && isEmpty((String) obj));
	}
	
	
	public static boolean isStringAndEmpty(Object obj) {
		return ((obj instanceof String) && isEmpty((String) obj));
	}
	
}
