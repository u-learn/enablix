package com.enablix.app.main;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.io.Resource;
import org.springframework.web.servlet.resource.PathResourceResolver;
import org.springframework.web.servlet.resource.ResourceResolverChain;

import com.enablix.commons.util.web.WebUtils;

public class EnablixPathResourceResolver extends PathResourceResolver {
	
	public Resource resolveResource(HttpServletRequest request, String requestPath, List<? extends Resource> locations,
			ResourceResolverChain chain) {
		return super.resolveResource(request, processPath(requestPath), locations, chain);
	}
	
	public String resolveUrlPath(String resourcePath, List<? extends Resource> locations, ResourceResolverChain chain) {
		return super.resolveUrlPath(processPath(resourcePath), locations, chain);
	}
	
	protected String processPath(String requestPath) {
		return WebUtils.removeTenantInfo(requestPath);
	}
	
}
