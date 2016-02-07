package com.enablix.commons.dms;

public class DMSUtil {

	private DMSUtil() {
		// only use static methods
	}
	
	public static String getDocStoreConfigKey(String docStoreType) {
		return DocumentStoreConstants.DOC_STORE_CONFIG_KEY_PREFIX + docStoreType; 
	}
	
}
