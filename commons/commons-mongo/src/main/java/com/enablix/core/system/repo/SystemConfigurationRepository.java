package com.enablix.core.system.repo;

import com.enablix.core.domain.config.Configuration;
import com.enablix.core.mongo.repository.BaseMongoRepository;

public interface SystemConfigurationRepository extends BaseMongoRepository<Configuration> {

	Configuration findByKey(String key);
	
}
