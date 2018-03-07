package com.enablix.commons.util;

import com.enablix.commons.constants.AppConstants;

public class EnvPropertiesUtil {

	private static EnvironmentProperties properties;
	
	private static String baseDir;
	
	private static String dataDir;
	
	public static void registerProperties(EnvironmentProperties properties) {
		EnvPropertiesUtil.properties = properties;
	}
	
	public static EnvironmentProperties getProperties() {
		return properties;
	}
	
	public static String getDataDirectory() {
		
		if (dataDir == null) {
			
			baseDir = getBaseDirectory();
			dataDir = System.getProperty(AppConstants.PROPERTY_DATA_DIR, null);
			
			if (dataDir == null) {
				dataDir = baseDir;
			}
		}
		
		return dataDir;
	}

	public static String getBaseDirectory() {
		
		if (baseDir == null) {
			baseDir = System.getProperty(AppConstants.PROPERTY_BASE_DIR, ".");
		}
		
		return baseDir; 
	}
	
}
