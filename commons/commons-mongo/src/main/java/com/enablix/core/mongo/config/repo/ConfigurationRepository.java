package com.enablix.core.mongo.config.repo;

import com.enablix.core.domain.config.Configuration;
import com.enablix.core.mongo.repository.BaseMongoRepository;

public interface ConfigurationRepository extends BaseMongoRepository<Configuration> {

	Configuration findByKey(String key);
	
}
