package com.enablix.core.security.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.enablix.core.security.web.UserAndRolesVO;

public interface GuestUserProvider {

	UserAndRolesVO getGuestUser(HttpServletRequest request);
	
	/**
	 * List of Ant patterns for urls supported i.e. the urls for which
	 * this instance provides guest url details
	 * 
	 * @return List<String>
	 */
	List<String> supportedRequestUrls();
	
}
