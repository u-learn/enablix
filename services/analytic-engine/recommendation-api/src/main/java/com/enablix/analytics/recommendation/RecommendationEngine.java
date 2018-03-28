package com.enablix.analytics.recommendation;

import org.springframework.data.domain.Page;

import com.enablix.core.api.ContentDataRef;
import com.enablix.data.view.DataView;

public interface RecommendationEngine {

	Page<ContentDataRef> getRecommendations(RecommendationContext request, DataView dataView);
	
}
