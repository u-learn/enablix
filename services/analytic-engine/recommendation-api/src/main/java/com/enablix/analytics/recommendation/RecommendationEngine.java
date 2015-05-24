package com.enablix.analytics.recommendation;

import java.util.List;

import com.enablix.core.api.ContentDataRef;

public interface RecommendationEngine {

	List<ContentDataRef> getRecommendations(RecommendationContext request);
	
}
