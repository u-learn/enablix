package com.enablix.core.security.auth.repo;

import java.util.List;

import com.enablix.core.domain.security.authorization.Role;
import com.enablix.core.mongo.repository.BaseMongoRepository;

public interface RoleRepository extends BaseMongoRepository<Role> {

	List<Role> findByIdentityIn(List<String> roleIdentity);
	
	List<Role> findByRoleNameIn(List<String> roleNames);
	
	List<Role> findByPermissions(String permission);
	
}
