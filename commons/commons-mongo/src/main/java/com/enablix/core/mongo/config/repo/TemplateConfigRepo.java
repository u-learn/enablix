package com.enablix.core.mongo.config.repo;

import com.enablix.core.domain.config.TemplateConfiguration;
import com.enablix.core.mongo.repository.BaseMongoRepository;

public interface TemplateConfigRepo extends BaseMongoRepository<TemplateConfiguration> {

	TemplateConfiguration findByScenario(String scenario);
}
