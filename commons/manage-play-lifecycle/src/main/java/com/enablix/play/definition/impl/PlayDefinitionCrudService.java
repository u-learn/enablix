package com.enablix.play.definition.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.service.MongoRepoCrudService;
import com.enablix.core.domain.play.PlayDefinition;
import com.enablix.core.mongo.repository.BaseMongoRepository;
import com.enablix.play.definition.repo.PlayDefinitionRepository;

@Component
public class PlayDefinitionCrudService extends MongoRepoCrudService<PlayDefinition> {

	@Autowired
	private PlayDefinitionRepository repo;
	
	@Override
	public BaseMongoRepository<PlayDefinition> getRepository() {
		return repo;
	}

	@Override
	public PlayDefinition findExisting(PlayDefinition t) {
		return repo.findByPlayTemplateId(t.getPlayTemplate().getId());
	}
	
	@Override
	protected PlayDefinition merge(PlayDefinition t, PlayDefinition existing) {
		existing.setPlayTemplate(t.getPlayTemplate());
		return existing;
	}

}
