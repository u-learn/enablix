package com.enablix.core.security;

import java.io.IOException;
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
import com.enablix.core.activity.audit.ActivityTrackingConstants;
import com.enablix.core.activity.audit.ActivityTrackingContext;

public class AuditTrackingContextInitFilter extends GenericFilterBean {

	public AuditTrackingContextInitFilter() {
	}
	
	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws ServletException, IOException {
		
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		
		try {
			Map<String, String> auditTrackingParams = new HashMap<>();
			
			for (String trackAttr: ActivityTrackingConstants.TRACKING_ATTRS) {

				// add request parameters first
				String attrValue = request.getParameter(trackAttr);
				if (attrValue != null) {
					auditTrackingParams.put(trackAttr, attrValue);
				}
				
				// request header values over-write the request parameters
				attrValue = request.getHeader(trackAttr);
				if (attrValue != null) {
					auditTrackingParams.put(trackAttr, attrValue);
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
