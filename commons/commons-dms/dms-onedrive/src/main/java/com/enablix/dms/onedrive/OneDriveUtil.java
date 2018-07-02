package com.enablix.dms.onedrive;

import com.enablix.core.domain.config.Configuration;

public class OneDriveUtil {

	public static String getFileLocationRelativeToBaseFolder(String fileLocation, String baseFolder) {
		
		String relativeLocation = fileLocation;
		
		if (fileLocation.startsWith(baseFolder)) {
			relativeLocation = fileLocation.substring(baseFolder.length());
		}
		
		return relativeLocation;
	}

	public static String createFileLocationWithBaseFolder(String fileLocation, String baseFolder) {
		return baseFolder + (fileLocation.startsWith("/") ? "" : "/") + fileLocation;
	}

	public static String getBaseFolder(Configuration config) {
		return config.getStringValue(OneDriveConstants.CFG_BASE_FOLDER_KEY);
	}
	
	public static String getDriveOwnerId(Configuration config) {
		return config.getStringValue(OneDriveConstants.CFG_DRIVE_OWNER_ID);
	}

	public static String getAppId(Configuration config) {
		return config.getStringValue(OneDriveConstants.CFG_APP_ID);
	}

	public static String getAppPassword(Configuration config) {
		return config.getStringValue(OneDriveConstants.CFG_APP_PWD_ENC);
	}

	public static String getDriveOrgId(Configuration config) {
		return config.getStringValue(OneDriveConstants.CFG_ORG_ID);
	}
	
}
