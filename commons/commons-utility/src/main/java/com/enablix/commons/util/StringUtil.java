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
	
}
