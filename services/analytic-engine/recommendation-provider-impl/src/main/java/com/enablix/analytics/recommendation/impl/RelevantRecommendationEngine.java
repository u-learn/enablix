package com.enablix.analytics.recommendation.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.enablix.analytics.recommendation.RecommendationContext;
import com.enablix.analytics.recommendation.RecommendationEngine;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.domain.content.UserContentRelevance;
import com.enablix.core.mongo.repo.UserContentRelevanceRepository;
import com.enablix.data.view.DataView;

public class RelevantRecommendationEngine implements RecommendationEngine {

	@Autowired
	private UserContentRelevanceRepository relevantContentRepo;
	
	@Override
	public Page<ContentDataRef> getRecommendations(RecommendationContext request, DataView dataView) {
		
		String userId = request.getUserContext().userId();
		
		if (userId != null) {
			
			PageRequest pageable = new PageRequest(request.getRequestContext().pageNo(), request.getRequestContext().pageSize(), 
					new Sort(Direction.DESC, "relevance"));
			Page<UserContentRelevance> contentPage = relevantContentRepo.findByUserIdAndLatestTrue(userId, pageable);
			
			if (contentPage != null) {
				
				String templateId = ProcessContext.get().getTemplateId();
				
				List<ContentDataRef> content = 
						contentPage.getContent().stream().map(
							(relContent) -> 
								ContentDataRef.createContentRef(
									templateId, relContent.getContentQId(), 
									relContent.getContentIdentity(), relContent.getContentTitle()))
						.collect(Collectors.toList());
				
				return new PageImpl<ContentDataRef>(content, pageable, contentPage.getTotalElements());
			}
		}
		
		return null;
	}

}
