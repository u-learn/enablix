package com.enablix.analytics.recommendation.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.Query;

import com.enablix.core.domain.reco.Recommendation;
import com.enablix.core.mongo.repository.BaseMongoRepository;

public interface RecommendationRepository extends BaseMongoRepository<Recommendation> {

	@Query("{ $and : [{'recommendationScope.userId' : ?0 }, "
			   + "{'recommendationScope.templateId' : ?1 }, "
			   + "{'recommendationScope.containerQId' : null }, "
			   + "{'recommendationScope.clientId' : null }, "
			   + "{'recommendationScope.contentIdentity' : null } ]}")
	Collection<Recommendation> findByUserIdAndTemplateId(String userId, String templateId, Sort sort);

	@Query("{ $and : [{'recommendationScope.userId' : ?0 }, "
			       + "{'recommendationScope.clientId' : null }, "
				   + "{'recommendationScope.templateId' : ?1 }, "
				   + "{'recommendationScope.containerQId' : ?2 }, "
				   + "{'recommendationScope.contentIdentity' : null } ]}")
	Collection<Recommendation> findByUserIdAndTemplateIdAndContainerQId(
			String userId, String templateId, String containerQId, Sort sort);
	
	@Query("{ $and : [{'recommendationScope.userId' : ?0 }, "
			   + "{'recommendationScope.clientId' : null }, "
			   + "{'recommendationScope.templateId' : ?1 }, "
			   + "{'recommendationScope.containerQId' : ?2 }, "
			   + "{'recommendationScope.contentIdentity' : ?3 } ]}")
	Collection<Recommendation> findByUserIdAndTemplateIdAndContainerQIdAndContentIdentity(
		String userId, String templateId, String containerQId, String contentIdentity, Sort sort);

	@Query("{ $and : [{'recommendationScope.templateId' : ?0 }, "
			   + "{'recommendationScope.clientId' : null }, "
			   + "{'recommendationScope.containerQId' : ?1 }, "
			   + "{'recommendationScope.contentIdentity' : ?2 } ]}")
	Collection<Recommendation> findByTemplateIdAndContainerQIdAndContentIdentity(
		String templateId, String containerQId, String contentIdentity, Sort sort);

	@Query("{ $and : [{'recommendationScope.templateId' : ?0 }, "
			   + "{'recommendationScope.clientId' : null }, "
			   + "{'recommendationScope.containerQId' : null }, "
			   + "{'recommendationScope.contentIdentity' : null } ]}")
	Collection<Recommendation> findByTemplateId(String templateId, Sort sort);

	@Query("{ $and : [{'recommendationScope.templateId' : ?0 }, "
				   + "{'recommendationScope.containerQId' : ?1 }, "
				   + "{'recommendationScope.contentIdentity' : null } ]}")
	Collection<Recommendation> findByTemplateIdAndContainerQId(String templateId, String containerQId, Sort sort);

	@Query("{ $and : [{'recommendationScope.userId' : ?0 }, "
			   + "{'recommendationScope.templateId' : ?1 }, "
			   + "{'recommendationScope.containerQId' : ?2 }, "
			   + "{'recommendationScope.contentIdentity' : ?3 },"
			   + "{'recommendedData.data.containerQId' : ?4 },"
			   + "{'recommendedData.data.instanceIdentity' : ?5 } ]}")
	Collection<Recommendation> findByRecommendationScopeAndRecommendationData(
			String userId, String templateId, String containerQId, 
			String contentIdentity, String recoContainerQId, String recoContentIdentity);
	
	Collection<Recommendation> findByRecommendationScopeClientId(String clientId, Sort sort);

	List<Recommendation> findByRecommendedDataDataInstanceIdentity(String contentIdentity);

}
