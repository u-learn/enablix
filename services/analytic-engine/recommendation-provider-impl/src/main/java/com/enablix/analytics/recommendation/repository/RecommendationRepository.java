package com.enablix.analytics.recommendation.repository;

import java.util.Collection;

import org.springframework.data.mongodb.repository.Query;

import com.enablix.core.domain.reco.Recommendation;
import com.enablix.core.mongo.repository.BaseMongoRepository;

public interface RecommendationRepository extends BaseMongoRepository<Recommendation> {

	@Query("{ $and : [{'recommendationScope.userId' : ?0 }, "
			   + "{'recommendationScope.templateId' : ?1 }, "
			   + "{'recommendationScope.containerQId' : {$exists : false} }, "
			   + "{'recommendationScope.contentIdentity' : {$exists: false} } ]}")
	Collection<Recommendation> findByUserIdAndTemplateId(String userId, String templateId);

	@Query("{ $and : [{'recommendationScope.userId' : ?0 }, "
				   + "{'recommendationScope.templateId' : ?1 }, "
				   + "{'recommendationScope.containerQId' : ?2 }, "
				   + "{'recommendationScope.contentIdentity' : {$exists : false} } ]}")
	Collection<Recommendation> findByUserIdAndTemplateIdAndContainerQId(
			String userId, String templateId, String containerQId);
	
	@Query("{ $and : [{'recommendationScope.userId' : ?0 }, "
			   + "{'recommendationScope.templateId' : ?1 }, "
			   + "{'recommendationScope.containerQId' : ?2 }, "
			   + "{'recommendationScope.contentIdentity' : ?3 } ]}")
	Collection<Recommendation> findByUserIdAndTemplateIdAndContainerQIdAndContentIdentity(
		String userId, String templateId, String containerQId, String contentIdentity);
	
}
