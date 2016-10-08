package com.enablix.content.mapping.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.service.MongoRepoCrudService;
import com.enablix.content.mapping.repo.ContentMappingRepository;
import com.enablix.core.domain.content.mapping.ContentContainerMappingDocument;
import com.enablix.core.mongo.repository.BaseMongoRepository;

@Component
public class ContentMappingCrudService extends MongoRepoCrudService<ContentContainerMappingDocument> {

	@Autowired
	private ContentMappingRepository repo;

	@Override
	public BaseMongoRepository<ContentContainerMappingDocument> getRepository() {
		return repo;
	}

	@Override
	protected ContentContainerMappingDocument merge(ContentContainerMappingDocument t, ContentContainerMappingDocument existing) {
		existing.setContainerMapping(t.getContainerMapping());
		return existing;
	}
	
	
}
