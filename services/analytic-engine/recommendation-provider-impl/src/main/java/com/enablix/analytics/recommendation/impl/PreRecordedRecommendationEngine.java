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
			recommendations = contentSpecificRecommendations(
					userId, templateId, containerQId, contentIdentity);
		} 
		
		if (CollectionUtil.isEmpty(recommendations) && 
				!StringUtil.isEmpty(containerQId) && StringUtil.isEmpty(contentIdentity)) {
			recommendations = containerSpecificRecommendation(userId, templateId, containerQId);
		} 
		
		if (CollectionUtil.isEmpty(recommendations)) {
			recommendations = generalRecommendations(userId, templateId);
		}

		if (recommendations != null) {
			for (Recommendation reco : recommendations) {
				recoContent.add(reco.getRecommendedData().getData());
			}
		}
		
		return recoContent;
	}

	private Collection<Recommendation> contentSpecificRecommendations(String userId, String templateId,
			String containerQId, String contentIdentity) {
		
		// user and content specific recommendation
		Collection<Recommendation> recommendations = repo.findByUserIdAndTemplateIdAndContainerQIdAndContentIdentity(
				userId, templateId, containerQId, contentIdentity);
		
		if (CollectionUtil.isEmpty(recommendations)) {
			// ignore user
			recommendations = repo.findByTemplateIdAndContainerQIdAndContentIdentity(
					templateId, containerQId, contentIdentity);
			
		}
		
		return recommendations;
	}

	private Collection<Recommendation> generalRecommendations(String userId, String templateId) {
		
		Collection<Recommendation> recommendations = repo.findByUserIdAndTemplateId(userId, templateId);
		
		if (CollectionUtil.isEmpty(recommendations)) {
			recommendations = repo.findByTemplateId(templateId);
		}
		
		return recommendations;
	}

	private Collection<Recommendation> containerSpecificRecommendation(String userId, String templateId,
			String containerQId) {

		// user and container specific
		Collection<Recommendation> recommendations = repo.findByUserIdAndTemplateIdAndContainerQId(
				userId, templateId, containerQId);
		
		if (CollectionUtil.isEmpty(recommendations)) {
			recommendations = repo.findByTemplateIdAndContainerQId(
					templateId, containerQId);
		}
		
		return recommendations;
	}

}
