package com.enablix.app.content.ui.reco;

import java.util.List;

import com.enablix.analytics.recommendation.builder.web.WebRecommendationRequest;
import com.enablix.app.content.ui.NavigableContent;
import com.enablix.core.api.ContentDataRef;
import com.enablix.data.view.DataView;

public interface RecommendedContentService {

	List<NavigableContent> getRecommendedContent(WebRecommendationRequest request, DataView dataView);
	
	List<ContentDataRef> getAIRecommendedContent(WebRecommendationRequest request, DataView dataView);
	
}
