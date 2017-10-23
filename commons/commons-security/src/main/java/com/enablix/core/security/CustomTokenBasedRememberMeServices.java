package com.enablix.core.security;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

public class CustomTokenBasedRememberMeServices extends TokenBasedRememberMeServices {

	@Autowired
	private LoginListener loginListener;
	
	@Autowired
	private LastRememberMeLoginCache lastLoginCache;
	
	public CustomTokenBasedRememberMeServices(String key, UserDetailsService userDetailsService) {
		super(key, userDetailsService);
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
	
	@Override
	protected UserDetails processAutoLoginCookie(String[] cookieTokens,
			HttpServletRequest request, HttpServletResponse response) {
		
		UserDetails userDetails = super.processAutoLoginCookie(cookieTokens, request, response);
		
		if (shouldAuditLogin(userDetails.getUsername())) {
			// audit user login via remember-me token only once a day
			loginListener.auditUserLogin(userDetails);
		}
		
		return userDetails;
	}
	
	private boolean shouldAuditLogin(String username) {
		
		Calendar currDate = Calendar.getInstance();
		int presentDayOfYear = currDate.get(Calendar.DAY_OF_YEAR);
		
		int lastLoginDayOfYear = lastLoginCache.getAndSetLastRememberMeLoginDay(username, presentDayOfYear);

		return lastLoginDayOfYear != presentDayOfYear;
	}
	
}
