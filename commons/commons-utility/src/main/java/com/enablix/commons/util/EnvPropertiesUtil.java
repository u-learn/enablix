package com.enablix.commons.util;

public class EnvPropertiesUtil {

	private static EnvironmentProperties properties;
	
	public static void registerProperties(EnvironmentProperties properties) {
		EnvPropertiesUtil.properties = properties;
	}
	
	public static EnvironmentProperties getProperties() {
		return properties;
	}
	
}
