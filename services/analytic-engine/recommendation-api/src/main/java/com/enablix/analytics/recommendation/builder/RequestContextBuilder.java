package com.enablix.analytics.recommendation.builder;

import com.enablix.analytics.recommendation.RecommendationRequest;
import com.enablix.analytics.recommendation.RequestContext;

public interface RequestContextBuilder<R extends RecommendationRequest> {

	RequestContext build(R request);
	
}
