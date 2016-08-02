package com.enablix.core.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import com.enablix.core.security.service.UserService;


public class GuestUserLoginFilter extends OncePerRequestFilter {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GuestUserLoginFilter.class);
	
	@Value("#{'${security.guest.user.login.urls:/ushare/*}'.split(',')}") 
	private List<String> guestLoginUrls;
	
	@Autowired
	private UserService userService;
	
	private List<AntPathRequestMatcher> guestUserAccessUrlMatchers;
	
	@PostConstruct
	public void init() {
		guestUserAccessUrlMatchers = new ArrayList<>();
		for (String guestUrl : guestLoginUrls) {
			guestUserAccessUrlMatchers.add(new AntPathRequestMatcher(guestUrl));
		}
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || isAnonymousUser(auth)) {
			for (AntPathRequestMatcher matcher : guestUserAccessUrlMatchers) {
				if (matcher.matches(request)) {
					loginGuestUser(request);
					break;
				}
			}
		}
		
		filterChain.doFilter(request, response);
	}

	private boolean isAnonymousUser(Authentication auth) {
		
		Object principal = auth.getPrincipal();
	    
		if (principal == null) {
	        return true;
	    }
	    
		if (principal instanceof String) {
	        return "anonymousUser".equals((String) principal);
		}
	    
		return false;
	}

	private void loginGuestUser(HttpServletRequest request) {
		
		UserDetails guestUser = userService.getGuestUser(request);
		
		if (guestUser != null) {
			
			Authentication auth = 
				  new UsernamePasswordAuthenticationToken(guestUser, null, guestUser.getAuthorities());
		
			SecurityContextHolder.getContext().setAuthentication(auth);
			
		} else {
			LOGGER.error("Guest user details not found for request: {}", request.getRequestURL());
		}
		
	}
	
}