package com.enablix.core.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.enablix.commons.constants.AppConstants;
import com.enablix.core.domain.security.authorization.UserProfile;
import com.enablix.core.security.service.EnablixUserService.LoggedInUser;

public class SecurityUtil {
	
	private static final UserDetails SYSTEM_USER = new UserDetails() {
		
		private static final long serialVersionUID = 1L;

		@Override
		public boolean isEnabled() {
			return true;
		}
		
		@Override
		public boolean isCredentialsNonExpired() {
			return true;
		}
		
		@Override
		public boolean isAccountNonLocked() {
			return true;
		}
		
		@Override
		public boolean isAccountNonExpired() {
			return true;
		}
		
		@Override
		public String getUsername() {
			return AppConstants.SYSTEM_USER_ID;
		}
		
		@Override
		public String getPassword() {
			return null;
		}
		
		@Override
		public Collection<? extends GrantedAuthority> getAuthorities() {
			return new ArrayList<>();
		}
	};

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
	
	public static boolean isSystemUser(Authentication auth) {
		
		Object principal = auth.getPrincipal();
	    
		if (principal == null) {
	        return false;
	    }
	    
		if (principal instanceof String) {
	        return AppConstants.SYSTEM_USER_ID.equals((String) principal);
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
		
		if (isSystemUser(auth)) {
			return true;
		}
		
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
		
		if (isSystemUser(auth)) {
			return true;
		}
		
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
	
	public static void loginSystemUser() {
		
		Authentication auth = new UsernamePasswordAuthenticationToken(
				SYSTEM_USER.getUsername(), SYSTEM_USER.getPassword(), SYSTEM_USER.getAuthorities());
		
		SecurityContextHolder.getContext().setAuthentication(auth);
	}
	
	public static void loginUser(UserDetails userDetails) {
		Authentication auth = new UsernamePasswordAuthenticationToken(
				userDetails, userDetails.getPassword(), userDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(auth);
	}
	
}
