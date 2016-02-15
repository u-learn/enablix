package com.enablix.core.mongo.config.repo;

import com.enablix.core.domain.config.EmailConfiguration;
import com.enablix.core.mongo.repository.BaseMongoRepository;

public interface EmailConfigRepo extends BaseMongoRepository<EmailConfiguration> {

	EmailConfiguration findByTenantId(String tenantId);
	
}

