package com.enablix.core.web.versioning;

public class ResourceVersion {

	private String resourceName;
	
	private String resourceVersion;
	
	public ResourceVersion() {
		// for ORMs
	}
	
	public ResourceVersion(String resourceName, String resourceVersion) {
		super();
		this.resourceName = resourceName;
		this.resourceVersion = resourceVersion;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public String getResourceVersion() {
		return resourceVersion;
	}

	public void setResourceVersion(String resourceVersion) {
		this.resourceVersion = resourceVersion;
	}
	
}
