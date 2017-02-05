package com.enablix.core.system.repo;

import com.enablix.core.domain.slackdtls.SlackAppDtls;
import com.enablix.core.mongo.repository.BaseMongoRepository;

public interface SlackAppDtlsRepository extends BaseMongoRepository<SlackAppDtls> {
	SlackAppDtls findByAppName(String appName);
}
