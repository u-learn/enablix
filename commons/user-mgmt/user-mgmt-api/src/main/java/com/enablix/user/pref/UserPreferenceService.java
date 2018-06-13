package com.enablix.user.pref;

import java.util.Collection;

import com.enablix.core.domain.preference.UserPreference;

public interface UserPreferenceService {

	Collection<UserPreference> getUserPreferences(String userId);
	
	Collection<UserPreference> userApplicablePreferences(String userId);

	void saveUserPreference(UserPreference userPref);
	
	UserPreference getUserApplicablePreference(String userId, String prefKey);
	
}
