package com.enablix.analytics.recommendation.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.enablix.analytics.recommendation.RecommendationContext;
import com.enablix.analytics.recommendation.RecommendationEngine;
import com.enablix.analytics.recommendation.repository.RecommendationRepository;
import com.enablix.commons.util.StringUtil;
import com.enablix.commons.util.collection.CollectionUtil;
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
			
			// user and content specific recommendation
			recommendations = repo.findByUserIdAndTemplateIdAndContainerQIdAndContentIdentity(
					userId, templateId, containerQId, contentIdentity);
			
			if (CollectionUtil.isEmpty(recommendations)) {
				// ignore user
				recommendations = repo.findByTemplateIdAndContainerQIdAndContentIdentity(
						templateId, containerQId, contentIdentity);
			}
			
		} else if (!StringUtil.isEmpty(containerQId) && StringUtil.isEmpty(contentIdentity)) {
			
			// user and container specific
			recommendations = repo.findByUserIdAndTemplateIdAndContainerQId(
					userId, templateId, containerQId);
			
			if (CollectionUtil.isEmpty(recommendations)) {
				recommendations = repo.findByTemplateIdAndContainerQId(
						templateId, containerQId);
			}
			
		} else {
			recommendations = repo.findByUserIdAndTemplateId(userId, templateId);
			
			if (CollectionUtil.isEmpty(recommendations)) {
				recommendations = repo.findByTemplateId(templateId);
			}
		}

		if (recommendations != null) {
			for (Recommendation reco : recommendations) {
				recoContent.add(reco.getRecommendedData().getData());
			}
		}
		
		return recoContent;
	}

}
