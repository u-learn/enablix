package com.enablix.content.mapping.repo;

import com.enablix.core.domain.content.mapping.ContentContainerMappingDocument;
import com.enablix.core.mongo.repository.BaseMongoRepository;

public interface ContentMappingRepository extends BaseMongoRepository<ContentContainerMappingDocument> {

	ContentContainerMappingDocument findByMapperIdAndContainerMappingQualifiedId(String mapperId, String qualifiedId);
	
}
