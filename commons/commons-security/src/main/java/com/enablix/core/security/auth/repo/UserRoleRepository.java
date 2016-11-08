package com.enablix.core.security.auth.repo;

import java.util.List;

import com.enablix.core.domain.security.authorization.UserRole;
import com.enablix.core.mongo.repository.BaseMongoRepository;

public interface UserRoleRepository extends BaseMongoRepository<UserRole> {

	UserRole findByUserIdentity(String userIdentity);
	
	List<UserRole> findByRolesIdIn(List<String> roleIds);
	
}
