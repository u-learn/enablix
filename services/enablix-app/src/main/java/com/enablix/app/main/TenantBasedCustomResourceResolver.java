package com.enablix.app.main;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.servlet.resource.PathResourceResolver;
import org.springframework.web.servlet.resource.ResourceResolverChain;

import com.enablix.commons.constants.AppConstants;
import com.enablix.commons.util.process.ProcessContext;

public class TenantBasedCustomResourceResolver extends PathResourceResolver {

	private static final Logger LOGGER = LoggerFactory.getLogger(TenantBasedCustomResourceResolver.class);

	@Value("${ui.resource.custom.pattern:**/css/**,**/messages/**,**/img/**}")
	private final String[] customResourcePatternArr = new String[0];
	private final PathMatcher pathMatcher = new AntPathMatcher();


	@Override
	protected Resource resolveResourceInternal(HttpServletRequest request, String requestPath, 
			List<? extends Resource> locations, ResourceResolverChain chain) {
		
		try {
		
			TenantIdHolder.setTenantId(request.getParameter(AppConstants.TENANT_ID_REQ_PARAM));
			
			Resource resource = super.resolveResourceInternal(request, requestPath, locations, chain);
			if (resource != null) {
				return resource;
			}
			
			return chain.resolveResource(request, requestPath, locations);
			
		} finally {
			TenantIdHolder.clear();
		}
	}
	
	protected Resource getResource(String resourcePath, Resource location) throws IOException {
		
		final String tenantId = resolveTenantId();
		
		if (tenantId != null && matchedFoundAgainstPattern(resourcePath)) {
			location = location.createRelative("/custom/" + tenantId + "/");
			LOGGER.debug("try custom location: {}", location);
		}
		
		Resource resource = super.getResource(resourcePath, location);
		LOGGER.debug("Resource path: {}", resource);
		
		return resource;
	}
	

	private Boolean matchedFoundAgainstPattern(final String requestPath) {
		for (final String customResourcePattern : customResourcePatternArr) {
			LOGGER.debug("Checking for requestPath: {} against pattern: {} ", requestPath, customResourcePattern);
			if (pathMatcher.isPattern(customResourcePattern)) {
				if (pathMatcher.match(customResourcePattern, requestPath)) {
					return true;
				}
			} else {
				if (requestPath.equalsIgnoreCase(customResourcePattern)) {
					return true;
				}
			}
		}
		return false;
	}

	private String resolveTenantId() {
		
		String tenantId = null;
		
		ProcessContext processContext = ProcessContext.get();
		
		if (processContext != null) {
			tenantId = processContext.getTenantId();
		} else {
			tenantId = TenantIdHolder.getTenantId();
		}
		
		return tenantId;
	}
	
	private static class TenantIdHolder {
		
		public static final ThreadLocal<String> THREAD_LOCAL_TENANT_ID = new ThreadLocal<String>();
		
		public static void setTenantId(String tenantId) {
			THREAD_LOCAL_TENANT_ID.set(tenantId);
		}
		
		public static String getTenantId() {
			return THREAD_LOCAL_TENANT_ID.get();
		}
		
		public static void clear() {
			THREAD_LOCAL_TENANT_ID.remove();
		}
		
	}

}
