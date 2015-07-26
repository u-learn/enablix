package com.enablix.analytics.recommendation.builder.web;

import com.enablix.analytics.recommendation.RecommendationRequest;
import com.enablix.analytics.web.request.WebContentRequest;

public class WebRecommendationRequest extends WebContentRequest implements RecommendationRequest {

	public WebRecommendationRequest(String containerQId) {
		this(containerQId, null);
	}
	
	public WebRecommendationRequest(String containerQId, String contentIdentity) {
		super(containerQId, contentIdentity);
	}
	
	public WebRecommendationRequest() { 
		this(null, null);
	}
	
}
