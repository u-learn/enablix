package com.enablix.app.content.ui.reco;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.analytics.recommendation.RecommendationContext;
import com.enablix.analytics.recommendation.RecommendationEngine;
import com.enablix.analytics.recommendation.builder.RecommendationContextBuilder;
import com.enablix.analytics.recommendation.builder.web.WebRecommendationRequest;
import com.enablix.app.content.label.ContentLabelResolver;
import com.enablix.app.content.label.PortalContentLabelResolver;
import com.enablix.app.content.ui.NavigableContent;
import com.enablix.app.content.ui.NavigableContentBuilder;
import com.enablix.core.api.ContentDataRef;

@Component
public class RecommendedContentServiceImpl implements RecommendedContentService {

	@Autowired
	private RecommendationEngine recoEngine;
	
	@Autowired
	private RecommendationContextBuilder<WebRecommendationRequest> recoCtxBuilder;
	
	@Autowired
	private NavigableContentBuilder navContentBuilder;
	
	private ContentLabelResolver labelResolver = new PortalContentLabelResolver();
	
	@Override
	public List<NavigableContent> getRecommendedContent(WebRecommendationRequest request) {
		
		RecommendationContext recoCtx = recoCtxBuilder.build(request);
		
		List<ContentDataRef> recommendations = recoEngine.getRecommendations(recoCtx);
		
		List<NavigableContent> navRecos = new ArrayList<>();
		
		for (ContentDataRef reco : recommendations) {
			navRecos.add(navContentBuilder.build(reco, labelResolver));
		}
		
		return navRecos;
	}

}
