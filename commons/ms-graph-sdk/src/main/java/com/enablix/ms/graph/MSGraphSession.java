package com.enablix.ms.graph;

import org.springframework.util.MultiValueMap;

public interface MSGraphSession {

	String getAccessToken();
	
	String getDriveBaseUrl();

	MultiValueMap<String, String> commonHeaders();

}
