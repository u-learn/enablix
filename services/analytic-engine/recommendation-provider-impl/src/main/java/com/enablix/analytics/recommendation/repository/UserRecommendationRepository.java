package com.enablix.analytics.recommendation.repository;

import java.util.Collection;

import com.enablix.core.domain.reco.UserRecommendation;
import com.enablix.core.mongo.repository.BaseMongoRepository;

public interface UserRecommendationRepository extends BaseMongoRepository<UserRecommendation> {

	Collection<UserRecommendation> findByUserIdAndRecommendedDataDataTemplateIdAndRecommendedDataDataContainerQId(
			String userId, String recommendedDataDataTemplateId, String recommendedDataDataContainerQId);
	
}
