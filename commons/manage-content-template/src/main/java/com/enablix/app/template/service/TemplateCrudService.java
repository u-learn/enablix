package com.enablix.app.template.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.mongo.repository.TemplateRepository;
import com.enablix.app.service.MongoRepoCrudService;
import com.enablix.core.domain.content.TemplateDocument;
import com.enablix.core.mongo.repository.BaseMongoRepository;

@Component
public class TemplateCrudService extends MongoRepoCrudService<TemplateDocument> {

	@Autowired
	private TemplateRepository templateRepo;
	
	@Override
	public BaseMongoRepository<TemplateDocument> getRepository() {
		return templateRepo;
	}

	@Override
	public TemplateDocument merge(TemplateDocument t, TemplateDocument existing) {
		existing.setTemplate(t.getTemplate());
		return existing;
	}

}
