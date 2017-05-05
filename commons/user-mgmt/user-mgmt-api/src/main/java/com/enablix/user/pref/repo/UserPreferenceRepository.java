package com.enablix.user.pref.repo;

import java.util.List;

import com.enablix.core.domain.preference.UserPreference;
import com.enablix.core.mongo.repository.BaseMongoRepository;

public interface UserPreferenceRepository extends BaseMongoRepository<UserPreference> {

	UserPreference findByUserIdAndKey(String userId, String key);
	
	List<UserPreference> findByUserId(String userId);

}
