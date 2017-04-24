package com.enablix.core.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

import com.enablix.commons.constants.AppConstants;
import com.enablix.commons.util.StringUtil;
import com.enablix.commons.util.process.RequestContext;
import com.enablix.commons.util.web.TenantInfo;
import com.enablix.commons.util.web.WebUtils;

public class RequestContextFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String tenantId = null;
		String clientId = null;
		
		TenantInfo tenantInfo = WebUtils.getTenantInfoFromUrl(request);
		if (tenantInfo != null) {
			tenantId = tenantInfo.getTenantId();
			clientId = tenantInfo.getClientId();
		}
		
		if (tenantId == null) {
			tenantId = getValueFromRequest(request, AppConstants.TENANT_ID_REQ_PARAM); 
		}
		
		if (clientId == null) {
			clientId = getValueFromRequest(request, AppConstants.CLIENT_ID_REQ_PARAM);
		}
		
		if (StringUtil.hasText(tenantId) || StringUtil.hasText(clientId)) {
			RequestContext.initialize(tenantId, clientId);
		}
		
		try {
			filterChain.doFilter(request, response);
			
		} finally {
			RequestContext.clear();
		}
		
	}
	
	private String getValueFromRequest(HttpServletRequest request, String key) {
		
		String value = request.getParameter(key);
		
		if (StringUtil.isEmpty(value)) {
			value = request.getHeader(key);
		}
		
		return value;
	}

}
