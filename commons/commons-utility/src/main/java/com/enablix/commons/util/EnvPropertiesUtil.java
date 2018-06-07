package com.enablix.commons.util;

import com.enablix.commons.constants.AppConstants;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.commons.util.process.RequestContext;

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
	
	public static String getDefaultServerUrl() {
		return properties.getSubdomainSpecificServerUrl(null);
	}
	
	public static String getSubdomainSpecificServerUrl() {
		
		String tenantId = null;
		
		RequestContext requestContext = RequestContext.get();
		if (requestContext != null) {
			tenantId = requestContext.getTenantId();
		}
		
		if (tenantId == null) {
			ProcessContext pc = ProcessContext.get();
			if (pc != null) {
				tenantId = pc.getTenantId();
			}
		}
		
		return properties.getSubdomainSpecificServerUrl(tenantId);
	}
	
}
