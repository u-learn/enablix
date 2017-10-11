package com.enablix.app.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.servlet.resource.ResourceResolverChain;

import com.enablix.commons.util.collection.CollectionUtil;

public class CustomResourceResolver extends EnablixPathResourceResolver {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomResourceResolver.class);
	
	@Value("${ui.resource.custom.pattern:**/messages/**,**/img/**,**/portal-home.html}")
	private final String[] customResourcePatternArr = new String[0];
	private final PathMatcher pathMatcher = new AntPathMatcher();
	
	private List<CustomResourcePathBuilder> customPathBuilders;
	
	public CustomResourceResolver(List<CustomResourcePathBuilder> customPathBuilders) {
		super();
		this.customPathBuilders = customPathBuilders;
	}

	@Override
	protected Resource resolveResourceInternal(HttpServletRequest request, 
			String requestPath, List<? extends Resource> locations, ResourceResolverChain chain) {
		
		Resource resource = super.resolveResourceInternal(request, requestPath, locations, chain);
		if (resource != null) {
			return resource;
		}
		
		return chain.resolveResource(request, requestPath, locations);
	}

	@Override
	protected Resource getResource(String resourcePath, Resource location) throws IOException {
		
		List<Resource> customLocations = resolveCustomLocations(location);
		Resource resource = null;
				
		if (CollectionUtil.isNotEmpty(customLocations) && matchedFoundAgainstPattern(resourcePath)) {
			
			for (Resource customLocation : customLocations) {
				
				LOGGER.debug("Custom Resource path: {}", resource);
				
				resource = super.getResource(resourcePath, customLocation);
				if (resource != null) {
					break;
				}
			}
		}
		
		return resource;
	}

	protected List<Resource> resolveCustomLocations(Resource location) throws IOException {
		
		List<Resource> customLocations = new ArrayList<>();
		
		for (CustomResourcePathBuilder pathBuilder : customPathBuilders) {
			Resource customLoc = pathBuilder.createCustomLocation(location);
			if (customLoc != null) {
				customLocations.add(customLoc);
			}
		}
		
		return customLocations;
	}

	private Boolean matchedFoundAgainstPattern(final String requestPath) {
		
		for (final String customResourcePattern : customResourcePatternArr) {
		
			LOGGER.debug("Checking for requestPath: {} against pattern: {} ", 
								requestPath, customResourcePattern);
			
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

}