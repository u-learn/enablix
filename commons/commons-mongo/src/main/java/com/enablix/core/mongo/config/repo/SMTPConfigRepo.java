package com.enablix.core.mongo.config.repo;


import com.enablix.core.domain.config.SMTPConfiguration;
import com.enablix.core.mongo.repository.BaseMongoRepository;

public interface SMTPConfigRepo extends BaseMongoRepository<SMTPConfiguration> {

	SMTPConfiguration findByDomainName(String domainName);

	
	
}
