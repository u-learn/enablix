package com.enablix.app.template.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.commons.caching.api.CachingService;
import com.enablix.commons.util.StringUtil;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.core.web.versioning.VersionedResource;

@Component
public class TemplateVersionManager implements VersionedResource {

	private static final String DEFAULT_TEMPLATE_VERSION = "0";

	private static final String TEMPLATE_VERSION_CACHE_KEY = "template.version";
	
	@Autowired
	private CachingService cache;
	
	@Override
	public String getResourceName() {
		return "content-template";
	}

	@Override
	public String getResourceVersion() {
		String tenantId = ProcessContext.get().getTenantId();
		String templateId = ProcessContext.get().getTemplateId();
		return (String) cache.get(tenantId, getTemplateVersionCacheKey(templateId));
	}
	
	public void updateTemplateVersion(ContentTemplate template) {
		
		String tenantId = ProcessContext.get().getTenantId();
		String version = template.getVersion();
		
		if (StringUtil.isEmpty(version)) {
			version = DEFAULT_TEMPLATE_VERSION;
		}
		
		cache.put(tenantId, getTemplateVersionCacheKey(template.getId()), version);
	}

	public String getTenantTemplateVersion(String tenantId, String templateId) {
		return (String) cache.get(tenantId, getTemplateVersionCacheKey(templateId));
	}
	
	private String getTemplateVersionCacheKey(String templateId) {
		return templateId + "." + TEMPLATE_VERSION_CACHE_KEY;
	}

	@Override
	public boolean isVersionCompatible(String version) {
		return version != null ? version.equals(getResourceVersion()) : false;
	}

}
