package com.enablix.core.security;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.GenericFilterBean;

import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.activity.audit.ActivityTrackingContext;

public class AuditTrackingContextInitFilter extends GenericFilterBean {

	
	private static final String AUDIT_TRACKING_PARAM_PREFIX = "at";

	public AuditTrackingContextInitFilter() {
	}
	
	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws ServletException, IOException {
		
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		
		try {
			Map<String, String> auditTrackingParams = new HashMap<>();
			
			Enumeration<String> paramNames = request.getParameterNames();
			while (paramNames.hasMoreElements()) {
				String paramName = paramNames.nextElement();
				if (paramName.startsWith(AUDIT_TRACKING_PARAM_PREFIX)) {
					auditTrackingParams.put(paramName, request.getParameter(paramName));
				}
			}
			
			Enumeration<String> headerNames = request.getHeaderNames();
			while (headerNames.hasMoreElements()) {
				String header = headerNames.nextElement();
				if (header.startsWith(AUDIT_TRACKING_PARAM_PREFIX)) {
					auditTrackingParams.put(header, request.getHeader(header));
				}
			}
			
			if (!auditTrackingParams.isEmpty()) {
				ActivityTrackingContext.initialize(auditTrackingParams);
			}
			
			chain.doFilter(request, response);
			
		} finally {
			ProcessContext.clear();
		}
		
	}

}
