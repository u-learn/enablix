package com.enablix.analytics.recommendation.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.enablix.analytics.recommendation.RecommendationContext;
import com.enablix.analytics.recommendation.RecommendationEngine;
import com.enablix.analytics.recommendation.repository.UserRecommendationRepository;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.domain.reco.UserRecommendation;

public class PreRecordedRecommendationEngine implements RecommendationEngine {

	@Autowired
	private UserRecommendationRepository repo;
	
	@Override
	public List<ContentDataRef> getRecommendations(RecommendationContext request) {
		
		List<ContentDataRef> recoContent = new ArrayList<>();
		
		Collection<UserRecommendation> recommendations = 
				repo.findByUserIdAndRecommendedDataDataTemplateIdAndRecommendedDataDataContainerQId(
				request.getUserContext().userId(), request.getRequestContext().templateId(),
				request.getRequestContext().containerQId());
		
		for (UserRecommendation reco : recommendations) {
			recoContent.add(reco.getRecommendedData().getData());
		}
		
		return recoContent;
	}

}
