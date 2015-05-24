package com.enablix.analytics.recommendation.builder;

import com.enablix.analytics.recommendation.RecommendationContext;
import com.enablix.analytics.recommendation.RecommendationRequest;

public interface RecommendationContextBuilder<R extends RecommendationRequest> {

	RecommendationContext build(R request);
	
}
