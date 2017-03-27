package com.enablix.core.security.auth.repo;

import java.util.List;

import com.enablix.core.domain.security.authorization.Role;
import com.enablix.core.domain.security.authorization.UserProfile;
import com.enablix.core.mongo.repository.BaseMongoRepository;

public interface UserProfileRepository extends BaseMongoRepository<UserProfile>{
	UserProfile findByEmail(String email);
	UserProfile findByUserIdentity(String userIdentity);
	List<UserProfile> findBySystemProfile_RolesIn(List<Role> roles);
	List<UserProfile> findAllByOrderByNameAsc();
	Long deleteByUserIdentity(String userIdentity);
}