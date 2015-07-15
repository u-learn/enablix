package com.enablix.analytics.recommendation.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.enablix.analytics.recommendation.RecommendationContext;
import com.enablix.analytics.recommendation.RecommendationEngine;
import com.enablix.analytics.recommendation.repository.RecommendationRepository;
import com.enablix.commons.util.StringUtil;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.domain.reco.Recommendation;

public class PreRecordedRecommendationEngine implements RecommendationEngine {

	@Autowired
	private RecommendationRepository repo;
	
	@Override
	public List<ContentDataRef> getRecommendations(RecommendationContext request) {
		
		List<ContentDataRef> recoContent = new ArrayList<>();
		Collection<Recommendation> recommendations = null;
		
		String userId = request.getUserContext().userId();
		String templateId = request.getRequestContext().templateId();
		
		String containerQId = request.getRequestContext().containerQId();
		String contentIdentity = request.getRequestContext().contentIdentity();
		
		if (!StringUtil.isEmpty(containerQId) && !StringUtil.isEmpty(contentIdentity)) {
			recommendations = repo.findByUserIdAndTemplateIdAndContainerQIdAndContentIdentity(
					userId, templateId, containerQId, contentIdentity);
			
		} else if (!StringUtil.isEmpty(containerQId) && StringUtil.isEmpty(contentIdentity)) {
			recommendations = repo.findByUserIdAndTemplateIdAndContainerQId(
					userId, templateId, containerQId);
			
		} else {
			recommendations = repo.findByUserIdAndTemplateId(userId, templateId);
		}

		if (recommendations != null) {
			for (Recommendation reco : recommendations) {
				recoContent.add(reco.getRecommendedData().getData());
			}
		}
		
		return recoContent;
	}

}
