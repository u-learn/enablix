package com.enablix.core.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import com.enablix.commons.constants.AppConstants;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.security.service.EnablixUserService.LoggedInUser;

public class ProcessContextInitFilter extends OncePerRequestFilter {

	private List<AntPathRequestMatcher> systemUserRequestMatchers;
	
	public ProcessContextInitFilter(String... systemUserRequestPatterns) {
		this.systemUserRequestMatchers = new ArrayList<>();
		for (String pattern : systemUserRequestPatterns) {
			systemUserRequestMatchers.add(new AntPathRequestMatcher(pattern));
		}
	}
	
	public ProcessContextInitFilter() {
		this(new String[0]);
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		
		SecurityContext securityCtx = SecurityContextHolder.getContext();
		
		boolean processCtxInitialized = false;
		
		if (securityCtx != null && securityCtx.getAuthentication() != null) {
			
			Object principal = securityCtx.getAuthentication().getPrincipal();
			
			if (principal instanceof LoggedInUser) {
				
				LoggedInUser user = (LoggedInUser) principal;
			
				ProcessContext.initialize(user.getUsername(), user.getUser().getDisplayName(),
						user.getUser().getTenantId(), user.getTemplateId());
				
				processCtxInitialized = true;
			}
			
		}
		
		if (!processCtxInitialized) {
			// check if the request is for system url, then init with system user
			for (AntPathRequestMatcher matcher : systemUserRequestMatchers) {
				
				if (matcher.matches(request)) {
					ProcessContext.initialize(AppConstants.SYSTEM_USER_ID, 
							AppConstants.SYSTEM_USER_NAME, null, null);
					break;
				}
			}
		}
		
		try {
			chain.doFilter(request, response);
			
		} finally {
			ProcessContext.clear();
		}
		
	}

}
