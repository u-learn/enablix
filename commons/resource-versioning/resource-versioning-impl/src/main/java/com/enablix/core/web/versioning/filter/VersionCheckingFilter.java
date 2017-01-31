package com.enablix.core.web.versioning.filter;

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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.enablix.core.security.SecurityUtil;
import com.enablix.core.web.versioning.VersionedResource;
import com.enablix.core.web.versioning.VersionedResourceRegistry;

@Component
public class VersionCheckingFilter extends OncePerRequestFilter {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(VersionCheckingFilter.class);
	
	
	public static final int SC_RESOURCE_VERSION_MISMATCH = 418;
	public static final String VERSION_HEADER_PREFIX = "version.";
	
	
	@Value("#{'${app.version.check.urls:/data/**}'.split(',')}") 
	private List<String> versionCheckUrls;
	
	private List<AntPathRequestMatcher> versionCheckUrlMatchers;
	
	@Autowired
	private VersionedResourceRegistry resourceRegistry;
	
	@PostConstruct
	public void init() {
		versionCheckUrlMatchers = new ArrayList<>();
		for (String checkUrl : versionCheckUrls) {
			versionCheckUrlMatchers.add(new AntPathRequestMatcher(checkUrl));
		}
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		boolean versionCheckFailed = false;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		if (auth != null && !SecurityUtil.isAnonymousUser(auth)) {
		
			for (AntPathRequestMatcher matcher : versionCheckUrlMatchers) {
			
				if (matcher.matches(request)) {
					
					if (!isResourceVersioningIncompatible(request, response)) {
						versionCheckFailed = true;
						break;
					}
				}
			}
		}
		
		if (!versionCheckFailed) {
			filterChain.doFilter(request, response);
		}
	}

	private boolean isResourceVersioningIncompatible(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		for (VersionedResource resource : resourceRegistry.getVersionedResources()) {
			
			String versionInHeader = request.getHeader(versionHeaderName(resource.getResourceName()));
			
			if (!resource.isVersionCompatible(versionInHeader)) {
				
				LOGGER.info("Version mis-match for resource {} - request version: {}, server version: {}", 
						resource.getResourceName(), versionInHeader, resource.getResourceVersion());
				
				response.sendError(SC_RESOURCE_VERSION_MISMATCH, "Resource version mis-match. Reload application.");
				return false;
			}
		}
		
		return true;
	}
	
	private String versionHeaderName(String resourceName) {
		return VERSION_HEADER_PREFIX + resourceName;
	}

}