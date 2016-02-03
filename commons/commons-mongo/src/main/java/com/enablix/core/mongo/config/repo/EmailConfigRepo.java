package com.enablix.core.mongo.config.repo;

import com.enablix.core.mongo.repository.BaseMongoRepository;

public interface EmailConfigRepo extends BaseMongoRepository<EmailConfiguration> {

	EmailConfiguration findByIdentity(String identity);
	
}

