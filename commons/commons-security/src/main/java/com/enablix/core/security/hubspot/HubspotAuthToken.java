package com.enablix.core.security.hubspot;

import org.springframework.data.mongodb.core.mapping.Document;

import com.enablix.core.domain.BaseDocumentEntity;

@Document(collection = "ebx_hubspot_auth_token")
public class HubspotAuthToken extends BaseDocumentEntity {

	private String tenantId;
	
	private String hubspotAppKey;
	
	private String oauth2AccessToken;

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public String getHubspotAppKey() {
		return hubspotAppKey;
	}

	public void setHubspotAppKey(String hubspotAppKey) {
		this.hubspotAppKey = hubspotAppKey;
	}

	public String getOauth2AccessToken() {
		return oauth2AccessToken;
	}

	public void setOauth2AccessToken(String oauth2AccessToken) {
		this.oauth2AccessToken = oauth2AccessToken;
	}
	
	
}
