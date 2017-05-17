package com.enablix.commons.util;

public class LocationUtil {

	public static String getBaseFolder(String location) {
		
		location = location.replace('\\', '/');
		String basePath = location;
		
		int lastIndexOf = location.lastIndexOf('/');
		
		if (lastIndexOf > 0) {
			basePath = location.substring(0, lastIndexOf);
		}
		
		return basePath;
	}
	
}
