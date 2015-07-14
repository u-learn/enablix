package com.enablix.analytics.recommendation.builder.web;

import com.enablix.analytics.recommendation.RecommendationRequest;

public class WebRecommendationRequest implements RecommendationRequest {

	private String containerQId;

	public WebRecommendationRequest(String containerQId) {
		super();
		this.containerQId = containerQId;
	}
	
	public WebRecommendationRequest() { }
	
	public String getContainerQId() {
		return containerQId;
	}

	public void setContainerQId(String containerQId) {
		this.containerQId = containerQId;
	}

	
	
}
