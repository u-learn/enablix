package com.enablix.core.mongo.config.repo;

import com.enablix.core.mongo.repository.BaseMongoRepository;

public interface SMTPConfigRepo extends BaseMongoRepository<SMTPConfiguration> {

	SMTPConfiguration findByIdentity(String identity);
}
