package com.enablix.analytics.recommendation;

import java.util.List;

import com.enablix.core.api.ContentDataRef;
import com.enablix.data.view.DataView;

public interface RecommendationEngine {

	List<ContentDataRef> getRecommendations(RecommendationContext request, DataView dataView);
	
}
