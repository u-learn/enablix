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

import com.enablix.commons.util.process.ProcessContext;

public class TenantBasedCustomResourceResolver extends PathResourceResolver {

	private static final Logger LOGGER = LoggerFactory.getLogger(TenantBasedCustomResourceResolver.class);

	@Value("${ui.resource.custom.pattern:**/css/**,**/messages/**,**/img/**}")
	private final String[] customResourcePatternArr = new String[0];
	private final PathMatcher pathMatcher = new AntPathMatcher();


	@Override
	protected Resource resolveResourceInternal(HttpServletRequest request, String requestPath, 
			List<? extends Resource> locations, ResourceResolverChain chain) {
		Resource resource = super.resolveResourceInternal(request, requestPath, locations, chain);
		if (resource != null) {
			return resource;
		}
		return chain.resolveResource(request, requestPath, locations);
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
		ProcessContext processContext = ProcessContext.get();
		if (processContext != null) {
			return processContext.getTenantId();
		}
		return null;
	}

}
