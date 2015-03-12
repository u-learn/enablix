package com.enablix.app.mongo.repository;

import com.enablix.core.domain.content.TemplateDocument;
import com.enablix.core.mongo.repository.BaseMongoRepository;

public interface TemplateRepository extends BaseMongoRepository<TemplateDocument> {

	TemplateDocument findByTemplateId(String templateId);
	
}
