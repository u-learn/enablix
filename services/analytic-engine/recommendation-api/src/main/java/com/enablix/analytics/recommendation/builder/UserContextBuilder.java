package com.enablix.analytics.recommendation.builder;

import com.enablix.analytics.recommendation.RecommendationRequest;
import com.enablix.analytics.recommendation.UserContext;

public interface UserContextBuilder<R extends RecommendationRequest> {

	UserContext build(R request);
	
}
