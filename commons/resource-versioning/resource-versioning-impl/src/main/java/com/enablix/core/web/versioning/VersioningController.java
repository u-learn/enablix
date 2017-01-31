package com.enablix.core.web.versioning;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("version")
public class VersioningController {
	
	@Autowired
	private VersionedResourceRegistry resourceRegistry;
	
	@RequestMapping(method = RequestMethod.GET, value="/all", produces="application/json")
	public Collection<ResourceVersion> getResourceVersions() throws ServletException, IOException {
		return resourceRegistry.getAllResourceVersions();
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/r/{resourceName}", produces="application/json")
	public ResourceVersion getResourceVersion(@PathVariable String resourceName) throws ServletException, IOException {
		VersionedResource resource = resourceRegistry.getVersionedResource(resourceName);
		String version = resource == null ? null : resource.getResourceVersion();
		return new ResourceVersion(resourceName, version); 
	}
	
}
