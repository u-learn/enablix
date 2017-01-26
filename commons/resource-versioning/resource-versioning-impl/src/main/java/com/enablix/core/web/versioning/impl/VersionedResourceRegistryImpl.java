package com.enablix.core.web.versioning.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.enablix.commons.util.beans.SpringBackedBeanRegistry;
import com.enablix.core.web.versioning.ResourceVersion;
import com.enablix.core.web.versioning.VersionedResource;
import com.enablix.core.web.versioning.VersionedResourceRegistry;

@Component
public class VersionedResourceRegistryImpl extends SpringBackedBeanRegistry<VersionedResource> implements VersionedResourceRegistry {

	private Map<String, VersionedResource> versionedResources = new HashMap<>();
	
	@Override
	protected Class<VersionedResource> lookupForType() {
		return VersionedResource.class;
	}

	public Collection<VersionedResource> getVersionedResources() {
		return versionedResources.values();
	}

	@Override
	public Collection<ResourceVersion> getAllResourceVersions() {
		
		Collection<ResourceVersion> resourceVersions = new ArrayList<>();
		
		for (VersionedResource resource : getVersionedResources()) {
			resourceVersions.add(new ResourceVersion(resource.getResourceName(), resource.getResourceVersion()));
		}
		
		return resourceVersions;
	}

	@Override
	public VersionedResource getVersionedResource(String resourceName) {
		return versionedResources.get(resourceName);
	}

	@Override
	protected void registerBean(VersionedResource bean) {
		versionedResources.put(bean.getResourceName(), bean);
	}

}
