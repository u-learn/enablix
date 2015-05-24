package com.enablix.app.content.ui.reco;

import java.util.List;

import com.enablix.analytics.recommendation.builder.web.WebRecommendationRequest;
import com.enablix.app.content.ui.NavigableContent;

public interface RecommendedContentService {

	List<NavigableContent> getRecommendedContent(WebRecommendationRequest request);
	
}
