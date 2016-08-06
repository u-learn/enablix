package com.enablix.core.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.enablix.commons.constants.AppConstants;
import com.enablix.core.security.service.EnablixUserService.LoggedInUser;

public class SecurityUtil {

	public static boolean isAnonymousUser(Authentication auth) {
		
		Object principal = auth.getPrincipal();
	    
		if (principal == null) {
	        return true;
	    }
	    
		if (principal instanceof String) {
	        return "anonymousUser".equals((String) principal);
		}
	    
		return false;
	}
	
	public static boolean isGuestUserLogIn() {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Object principal = auth.getPrincipal();
	    
		if (principal == null) {
	        return false;
	    }
	    
		if (principal instanceof LoggedInUser) {
			LoggedInUser liUser = (LoggedInUser) principal;
			return AppConstants.GUEST_USER_IDENTITY.equals(liUser.getUser().getIdentity());
		}
	    
		return false;
	}
	
}
