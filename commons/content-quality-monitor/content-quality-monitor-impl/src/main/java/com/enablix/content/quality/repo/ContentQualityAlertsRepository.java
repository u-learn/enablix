package com.enablix.content.quality.repo;

import com.enablix.core.domain.content.quality.ContentQualityAnalysis;
import com.enablix.core.mongo.repository.BaseMongoRepository;

@Deprecated
public interface ContentQualityAlertsRepository extends BaseMongoRepository<ContentQualityAnalysis> {

	ContentQualityAnalysis findByContentIdentity(String contentIdentity);
	
	Long deleteByContentIdentity(String contentIdentity);

}
