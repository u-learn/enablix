package com.enablix.core.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.annotation.PostConstruct;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.enablix.commons.util.web.TenantInfo;
import com.enablix.commons.util.web.WebUtils;
import com.enablix.core.security.hubspot.HubspotAuthToken;
import com.enablix.core.security.hubspot.repo.HubspotAuthTokenRepository;

@Component
@Order(-1000)
public class HubspotAuthFilter extends OncePerRequestFilter {

	private static final Logger LOGGER = LoggerFactory.getLogger(HubspotAuthFilter.class);
	
	@Autowired
	private HubspotAuthTokenRepository authTokenRepo;
	
	@Value("#{'${security.hubspot.in.request.urls:/**/hbspt/**/*}'.split(',')}") 
	private List<String> hubspotUrls;
	
	private List<AntPathRequestMatcher> hubspotUrlMatchers;
	
	@PostConstruct
	public void init() {
		hubspotUrlMatchers = new ArrayList<>();
		for (String guestUrl : hubspotUrls) {
			hubspotUrlMatchers.add(new AntPathRequestMatcher(guestUrl));
		}
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		// TODO: Validate hubspot request
		// TODO: if invalid then send 401 response
		// TODO: Get OAuth2 token for hubspot request
		
		String requestURI = request.getRequestURI();
		TenantInfo tenantInfo = WebUtils.getTenantInfoFromUrl(request);

		if (tenantInfo == null) {
			LOGGER.error("Tenant info not found in url [{}]", requestURI);
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		} 
		
		String tenantId = tenantInfo.getTenantId();
		
		HubspotAuthToken authToken = authTokenRepo.findByTenantId(tenantId);
		
		if (authToken == null) {
			LOGGER.error("Auth token not found for tenant [{}]", tenantId);
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}
		
		LOGGER.debug("OAuth2 token found for hubspot integration");
		
		//String hubspotAppKey = authToken.getHubspotAppKey();
		
		// TODO: validate request using hubspot app key
		String oauth2Token = authToken.getOauth2AccessToken();
		HubspotHttpRequest hubspotRequest = new HubspotHttpRequest(request, oauth2Token);
		filterChain.doFilter(hubspotRequest, response);

	}
	
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		if (auth == null || SecurityUtil.isAnonymousUser(auth)) {
			
			for (AntPathRequestMatcher matcher : hubspotUrlMatchers) {
				if (matcher.matches(request)) {
					return false;
				}
			}
			
		}
		
		return true;
	}

	private static class HubspotHttpRequest extends HttpServletRequestWrapper {
		
		private String bearerHeader;
		private Enumeration<String> authHeaderEnum;
		private Enumeration<String> allHeadersEnum;
		
		public HubspotHttpRequest(HttpServletRequest request, String oauth2Token) {
			
			super(request);
			
			this.bearerHeader = SecurityConstant.BEARER_TOKEN + " " + oauth2Token;
			
			Vector<String> bearerHeaders = new Vector<String>();
			bearerHeaders.add(bearerHeader);
			
			this.authHeaderEnum = bearerHeaders.elements();
			
			Set<String> headers = new HashSet<String>();
			headers.add(SecurityConstant.AUTHORIZATION_HEADER);
			
			Enumeration<String> headerNames = request.getHeaderNames();
			while (headerNames.hasMoreElements()) {
				headers.add(headerNames.nextElement());
			}
			
			allHeadersEnum = new Vector<String>(headers).elements();
		}

		@Override
	    public String getHeader(String name) {
			if (name.equalsIgnoreCase(SecurityConstant.AUTHORIZATION_HEADER)) {
				return bearerHeader;
			}
	        return super.getHeader(name);
	    }
		
		@Override
		public Enumeration<String> getHeaders(String name) {
			if (name.equalsIgnoreCase(SecurityConstant.AUTHORIZATION_HEADER)) {
				return authHeaderEnum;
			}
			return super.getHeaders(name);
		} 
		
		@Override
	    public Enumeration<String> getHeaderNames() {
	        return allHeadersEnum;
	    }
		
	}
	
}