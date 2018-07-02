package com.enablix.ms.graph.impl;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.enablix.ms.graph.MSGraphSession;
import com.enablix.ms.graph.model.OAuth20Token;

public class MSGraphOAuth20AppSession implements MSGraphSession {

	private static final String GRAPH_DRIVE_BASE_URL = "https://graph.microsoft.com/v1.0/users/%s/drive"; 
	
	private String accessToken;
	
	private String orgId;
	
	private String driveOwnerId;
	
	private String driveBaseUrl;
	
	public MSGraphOAuth20AppSession(String accessToken, String orgId, String driveOwnerId) {
		super();
		this.accessToken = accessToken;
		this.orgId = orgId;
		this.driveOwnerId = driveOwnerId;
		this.driveBaseUrl = String.format(GRAPH_DRIVE_BASE_URL, driveOwnerId);
	}

	public MSGraphOAuth20AppSession(OAuth20Token oauth20Token, String orgId, String driveOwnerId) {
		this(oauth20Token.getAccess_token(), orgId, driveOwnerId);
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	@Override
	public String getAccessToken() {
		return accessToken;
	}

	public String getOrgId() {
		return orgId;
	}

	public String getDriveOwnerId() {
		return driveOwnerId;
	}

	@Override
	public String getDriveBaseUrl() {
		return driveBaseUrl;
	}

	@Override
	public MultiValueMap<String, String> commonHeaders() {
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		headers.set("Authorization", "Bearer " + getAccessToken());
		return headers;
	}
	
}
