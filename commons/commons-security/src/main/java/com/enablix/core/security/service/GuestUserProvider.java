package com.enablix.core.security.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.enablix.core.domain.security.authorization.UserProfile;
import com.enablix.core.domain.user.User;

public interface GuestUserProvider {

	User getGuestUser(HttpServletRequest request);
	
	UserProfile getGuestUserProfile(User user);
	
	/**
	 * List of Ant patterns for urls supported i.e. the urls for which
	 * this instance provides guest url details
	 * 
	 * @return List<String>
	 */
	List<String> supportedRequestUrls();
	
}
