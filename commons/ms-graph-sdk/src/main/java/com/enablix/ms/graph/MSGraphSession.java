package com.enablix.ms.graph;

import java.util.Date;

import org.springframework.util.MultiValueMap;

public interface MSGraphSession {

	String getAccessToken();
	
	String getDriveBaseUrl();

	MultiValueMap<String, String> commonHeaders();
	
	Date expiresAt();

}
