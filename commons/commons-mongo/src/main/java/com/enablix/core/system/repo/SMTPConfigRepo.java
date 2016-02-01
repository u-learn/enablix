package com.enablix.core.system.repo;


import com.enablix.core.domain.config.SMTPConfiguration;
import com.enablix.core.mongo.repository.BaseMongoRepository;

public interface SMTPConfigRepo extends BaseMongoRepository<SMTPConfiguration> {

	SMTPConfiguration findByIdentity(String identity);
}
