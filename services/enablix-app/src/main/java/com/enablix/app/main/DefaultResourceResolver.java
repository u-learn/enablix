package com.enablix.app.main;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.io.Resource;
import org.springframework.web.servlet.resource.ResourceResolverChain;

public class DefaultResourceResolver extends EnablixPathResourceResolver {

	@Override
	protected Resource resolveResourceInternal(HttpServletRequest request, 
			String requestPath, List<? extends Resource> locations, ResourceResolverChain chain) {
		
		Resource resource = super.resolveResourceInternal(request, requestPath, locations, chain);
		if (resource != null) {
			return resource;
		}
		
		String lowerReqPath = requestPath.toLowerCase();
		if (lowerReqPath.startsWith("portal") || lowerReqPath.startsWith("login")) {
			resource = super.resolveResource(request, "/app2.html", locations, chain);
			if (resource != null) {
				return resource;
			}
		}
		
		return chain.resolveResource(request, requestPath, locations);
	}
}
