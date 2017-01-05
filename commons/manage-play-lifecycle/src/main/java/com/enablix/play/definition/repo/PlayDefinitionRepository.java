package com.enablix.play.definition.repo;

import com.enablix.core.domain.play.PlayDefinition;
import com.enablix.core.mongo.repository.BaseMongoRepository;

public interface PlayDefinitionRepository extends BaseMongoRepository<PlayDefinition> {

	PlayDefinition findByPlayTemplateId(String playTemplateId);
	
}
