package com.enablix.dms.sharepoint.service;

import java.util.Map;

public interface SharepointSession {

	Map<String, String> getRequestHeaders();
	
	String getSiteUrl();
	
	String getEndpointUrl();
	
	String getBaseFolder();

}
