package com.enablix.user.pref.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.service.MongoRepoCrudService;
import com.enablix.core.domain.preference.UserPreference;
import com.enablix.core.mongo.repository.BaseMongoRepository;
import com.enablix.user.pref.repo.UserPreferenceRepository;

@Component
public class UserPreferenceCrudService extends MongoRepoCrudService<UserPreference> {

	@Autowired
	private UserPreferenceRepository userPrefRepo;
	
	@Override
	public BaseMongoRepository<UserPreference> getRepository() {
		return userPrefRepo;
	}

	@Override
	public UserPreference merge(UserPreference t, UserPreference existing) {
		existing.setConfig(t.getConfig());
		return existing;
	}
	
	@Override
	public UserPreference findExisting(UserPreference t) {
		return userPrefRepo.findByUserIdAndKey(t.getUserId(), t.getKey());
	}

}
