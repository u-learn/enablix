package com.enablix.app.content.repository;

import java.util.Collection;

import com.enablix.core.domain.content.recent.RecentlyUpdatedContent;
import com.enablix.core.mongo.repository.BaseMongoRepository;

public interface RecentlyUpdatedContentRepository extends BaseMongoRepository<RecentlyUpdatedContent> {

	Collection<RecentlyUpdatedContent> findByBaseContainerQIdAndUpdatedDataTemplateId(
			String baseContainerQId, String updatedDataTemplateId);
	
	Collection<RecentlyUpdatedContent> findByUpdatedDataTemplateId(String updatedDataTemplateId);
	
	
}
