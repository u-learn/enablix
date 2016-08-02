package com.enablix.core.security.service;

import javax.servlet.http.HttpServletRequest;

public interface GuestUserProviderFactory {

	GuestUserProvider getProvider(HttpServletRequest request);
	
}
