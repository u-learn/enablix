package com.enablix.app.version;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.enablix.core.web.versioning.VersionedResource;

@Component
public class AppVersion implements VersionedResource {

	@Value("${app.version:1.0.0}")
	private String appVersion;
	
	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion; 
	}
	
	public String getAppVersion() {
		return appVersion;
	}
	
	@Override
	public String getResourceName() {
		return "webapp";
	}

	@Override
	public String getResourceVersion() {
		return appVersion;
	}

	@Override
	public boolean isVersionCompatible(String version) {
		return appVersion.equals(version);
	}
	
}
