package com.enablix.user.pref.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.commons.constants.AppConstants;
import com.enablix.commons.util.StringUtil;
import com.enablix.core.domain.preference.UserPreference;
import com.enablix.user.pref.UserPreferenceService;
import com.enablix.user.pref.repo.UserPreferenceRepository;

@Component
public class UserPreferenceServiceImpl implements UserPreferenceService {

	@Autowired
	private UserPreferenceRepository repo;
	
	@Autowired
	private UserPreferenceCrudService crud;
	
	@Override
	public Collection<UserPreference> getUserPreferences(String userId) {
		return repo.findByUserId(userId);
	}

	@Override
	public Collection<UserPreference> userApplicablePreferences(String userId, String clientId) {
		
		Collection<UserPreference> userPreferences = getUserPreferences(userId);
		Collection<UserPreference> systemPreferences = getUserPreferences(AppConstants.SYSTEM_USER_ID);
		
		if (StringUtil.hasText(clientId)) {
			Collection<UserPreference> clientPreference = getUserPreferences(clientId);
			systemPreferences = merge(systemPreferences, clientPreference);
		}
		 
		return merge(systemPreferences, userPreferences);
	}
	
	@Override
	public void saveUserPreference(UserPreference userPref) {
		crud.saveOrUpdate(userPref);
	}

	private Collection<UserPreference> merge(Collection<UserPreference> systemPreferences,
			Collection<UserPreference> userPreferences) {
		
		Map<String, UserPreference> keyToPref = new HashMap<>();
		
		systemPreferences.forEach((pref) -> keyToPref.put(pref.getKey(), pref));
		
		// override system preferences with user preferences
		userPreferences.forEach((pref) -> keyToPref.put(pref.getKey(), pref));
		
		return keyToPref.values();
	}

	@Override
	public UserPreference getUserApplicablePreference(String userId, String prefKey) {
		UserPreference pref = repo.findByUserIdAndKey(userId, prefKey);
		if (pref == null) {
			pref = repo.findByUserIdAndKey(AppConstants.SYSTEM_USER_ID, prefKey);
		}
		return pref;
	}

}
