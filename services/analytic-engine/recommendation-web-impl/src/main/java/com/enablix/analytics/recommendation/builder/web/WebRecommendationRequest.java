package com.enablix.analytics.recommendation.builder.web;

import com.enablix.analytics.recommendation.RecommendationRequest;

public class WebRecommendationRequest implements RecommendationRequest {

	private String containerQId;
	
	private String contentIdentity;

	public WebRecommendationRequest(String containerQId) {
		this(containerQId, null);
	}
	
	public WebRecommendationRequest(String containerQId, String contentIdentity) {
		super();
		this.containerQId = containerQId;
		this.contentIdentity = contentIdentity;
	}
	
	public WebRecommendationRequest() { 
		this(null, null);
	}
	
	public String getContainerQId() {
		return containerQId;
	}

	public void setContainerQId(String containerQId) {
		this.containerQId = containerQId;
	}

	public String getContentIdentity() {
		return contentIdentity;
	}

	public void setContentIdentity(String contentIdentity) {
		this.contentIdentity = contentIdentity;
	}

	
	
}
