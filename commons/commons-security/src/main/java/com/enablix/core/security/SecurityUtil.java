package com.enablix.core.security;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.enablix.commons.constants.AppConstants;
import com.enablix.core.domain.security.authorization.UserProfile;
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
	
	public static boolean currentUserHasPermission(String permission) {
		return currentUserHasAnyPermission(permission);
	}
	
	public static boolean currentUserHasAnyPermission(String... permissions) {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Set<String> authorities = AuthorityUtils.authorityListToSet(auth.getAuthorities());
		
		for (String perm : permissions) {
			if (authorities.contains(perm)) {
				return true;
			}
		}
		
		return false;
	}
	
	public static boolean currentUserHasAllPermission(String... permissions) {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Set<String> authorities = AuthorityUtils.authorityListToSet(auth.getAuthorities());
		
		for (String perm : permissions) {
			if (!authorities.contains(perm)) {
				return false;
			}
		}
		
		return true;
	}
	
	public static boolean userHasAllPermission(UserProfile userProfile, String... permissions) {
		
		Set<String> allPerms = new HashSet<>();
		userProfile.getSystemProfile().getRoles().forEach((role) -> allPerms.addAll(role.getPermissions()));
		
		for (String perm : permissions) {
			if (!allPerms.contains(perm)) {
				return false;
			}
		}
		
		return true;
	}
	
	public static UserDetails currentUser() {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Object principal = auth.getPrincipal();
	    
		return principal instanceof UserDetails ? (UserDetails) principal : null;
	}
	
}
