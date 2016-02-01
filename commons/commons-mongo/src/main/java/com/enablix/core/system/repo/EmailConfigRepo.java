package com.enablix.core.system.repo;

import com.enablix.core.domain.config.EmailConfiguration;
import com.enablix.core.mongo.repository.BaseMongoRepository;

public interface EmailConfigRepo extends BaseMongoRepository<EmailConfiguration> {

	EmailConfiguration findByIdentity(String identity);
	
}

