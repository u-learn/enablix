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
	
	public static String trimCharacter(String str, char ch) {
		
		int len = str.length();
        int st = 0;
        
        char[] val = str.toCharArray(); 

        while ((st < len) && (val[st] == ch)) {
            st++;
        }
        while ((st < len) && (val[len - 1] == ch)) {
            len--;
        }
        
        return ((st > 0) || (len < str.length())) ? str.substring(st, len) : str;
        
	}
	
}
