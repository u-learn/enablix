package com.enablix.core.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.enablix.commons.constants.AppConstants;
import com.enablix.commons.util.StringUtil;
import com.enablix.commons.util.process.RequestContext;
import com.enablix.commons.util.web.TenantInfo;
import com.enablix.commons.util.web.WebUtils;
import com.enablix.tenant.TenantManager;

@Component
@Order(-1010)
public class RequestContextInitFilter extends OncePerRequestFilter {

	@Autowired
	private TenantManager tenantMgr;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String requestURL = request.getRequestURL().toString();
		if (requestURL.contains("slackredirect")) {
			String stateParam = request.getParameter("state");
			if (StringUtil.hasText(stateParam)) {
				String redirectUrl = requestURL.replace("slackredirect", stateParam) 
						+ "#/account/slackdtls?" + request.getQueryString();
				response.sendRedirect(redirectUrl);
				return;
			}
		}
		
		String tenantId = null;
		String clientId = null;
		
		TenantInfo tenantInfo = WebUtils.getTenantInfoFromUrl(request);
		if (tenantInfo != null) {
			
			tenantId = tenantInfo.getTenantId();
			
			if (tenantId != null && !tenantMgr.doesTenantExist(tenantId)) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
				return;
			}
			
			clientId = tenantInfo.getClientId();
		}
		
		if (StringUtil.hasText(tenantId) && request.getRequestURI().equals("/")) {
			String appUrl = request.getRequestURL() + "app.html#/portal/home";
			response.sendRedirect(appUrl);
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
