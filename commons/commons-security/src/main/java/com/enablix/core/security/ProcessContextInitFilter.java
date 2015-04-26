package com.enablix.core.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.security.service.EnablixUserService.LoggedInUser;

public class ProcessContextInitFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		
		SecurityContext securityCtx = SecurityContextHolder.getContext();
		
		if (securityCtx != null && securityCtx.getAuthentication() != null) {
			
			Object principal = securityCtx.getAuthentication().getPrincipal();
			
			if (principal instanceof LoggedInUser) {
				
				LoggedInUser user = (LoggedInUser) principal;
				
				ProcessContext.initialize(user.getUsername(), 
						user.getUser().getTenantId(), user.getTemplateId());
			}
			
		}
		
		try {
			chain.doFilter(request, response);
			
		} finally {
			ProcessContext.clear();
		}
		
	}

}
