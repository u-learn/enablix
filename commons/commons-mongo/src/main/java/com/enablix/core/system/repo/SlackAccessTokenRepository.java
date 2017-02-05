package com.enablix.core.system.repo;

import com.enablix.core.domain.slackdtls.SlackAccessToken;
import com.enablix.core.mongo.repository.BaseMongoRepository;

public interface SlackAccessTokenRepository extends BaseMongoRepository<SlackAccessToken>{
	SlackAccessToken findByUserID(String userID);
}