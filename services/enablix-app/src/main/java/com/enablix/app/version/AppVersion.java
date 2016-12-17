package com.enablix.app.version;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.enablix.commons.caching.api.CachingService;

@Component
public class AppVersion {

	private static final String TEMPLATE_VERSION_CACHE_KEY = "template.version";
	
	@Autowired
	private CachingService cache;
	
	@Value("${app.version:1.0.0}")
	private String appVersion;
	
	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion; 
	}
	
	public String getAppVersion() {
		return appVersion;
	}
	
	public void setTenantTemplateVersion(String tenantId, String templateId, String version) {
		cache.put(tenantId, getTemplateVersionCacheKey(templateId), version);
	}
	
	public String getTenantTemplateVersion(String tenantId, String templateId) {
		return (String) cache.get(tenantId, getTemplateVersionCacheKey(templateId));
	}
	
	private String getTemplateVersionCacheKey(String templateId) {
		return templateId + "." + TEMPLATE_VERSION_CACHE_KEY;
	}
	
}
