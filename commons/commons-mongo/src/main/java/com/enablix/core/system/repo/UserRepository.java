package com.enablix.core.system.repo;

import java.util.List;

import com.enablix.core.domain.user.User;
import com.enablix.core.mongo.repository.BaseMongoRepository;

public interface UserRepository extends BaseMongoRepository<User> {

	User findByUserId(String userId);
	
	User findByIdentityAndTenantId(String identity, String tenantId);
	
	List<User> findByTenantId(String tenantId);
	
	Long deleteByUserId(String userId);

}
