package com.enablix.analytics.recommendation.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.enablix.analytics.recommendation.RecommendationContext;
import com.enablix.analytics.recommendation.RecommendationEngine;
import com.enablix.analytics.recommendation.repository.RecommendationRepository;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.domain.reco.Recommendation;

public class PreRecordedRecommendationEngine implements RecommendationEngine {

	@Autowired
	private RecommendationRepository repo;
	
	@Override
	public List<ContentDataRef> getRecommendations(RecommendationContext request) {
		
		List<ContentDataRef> recoContent = new ArrayList<>();
		
		Collection<Recommendation> recommendations = 
				repo.findByUserIdAndTemplateIdAndContainerQId(
				request.getUserContext().userId(), request.getRequestContext().templateId(),
				request.getRequestContext().containerQId());
		
		for (Recommendation reco : recommendations) {
			recoContent.add(reco.getRecommendedData().getData());
		}
		
		return recoContent;
	}

}
