package com.enablix.core.system.repo;

import com.enablix.core.domain.user.User;
import com.enablix.core.mongo.repository.BaseMongoRepository;

public interface UserRepository extends BaseMongoRepository<User> {

	User findByUserId(String userId);
	
}
