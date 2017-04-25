package com.enablix.core.domain.reco;

public class RecommendationScope {

	private String userId;
	
	private String templateId;
	
	private String containerQId;
	
	private String contentIdentity;
	
	private String clientId;

	public String getUserId() {
		return userId;
	}

	public String getTemplateId() {
		return templateId;
	}

	public String getContainerQId() {
		return containerQId;
	}

	public String getContentIdentity() {
		return contentIdentity;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public void setContainerQId(String containerQId) {
		this.containerQId = containerQId;
	}

	public void setContentIdentity(String contentIdentity) {
		this.contentIdentity = contentIdentity;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	
}
