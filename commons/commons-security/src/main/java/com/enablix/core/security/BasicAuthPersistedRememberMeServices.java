package com.enablix.core.security;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

public class BasicAuthPersistedRememberMeServices extends PersistentTokenBasedRememberMeServices {
	
	public BasicAuthPersistedRememberMeServices(String key, UserDetailsService userDetailsService,
			PersistentTokenRepository tokenRepository) {
		super(key, userDetailsService, tokenRepository);
	}
	
	@Override
	protected boolean rememberMeRequested(HttpServletRequest request, String parameter) {
		
		if (!super.rememberMeRequested(request, parameter)) {
			
			String headerValue = request.getHeader(parameter);

			if (headerValue != null) {
				if (headerValue.equalsIgnoreCase("true") || headerValue.equalsIgnoreCase("on")
						|| headerValue.equalsIgnoreCase("yes") || headerValue.equals("1")) {
					return true;
				}
			}
			
			if (logger.isDebugEnabled()) {
				logger.debug("Did not send remember-me cookie (principal did not set header '"
						+ parameter + "')");
			}
			
		} else {
			return true;
		}

		return false;
	}

}
