package com.enablix.core.web.versioning;

import java.util.Collection;

public interface VersionedResourceRegistry {

	VersionedResource getVersionedResource(String resourceName);
	
	Collection<VersionedResource> getVersionedResources();
	
	Collection<ResourceVersion> getAllResourceVersions();
	
}
