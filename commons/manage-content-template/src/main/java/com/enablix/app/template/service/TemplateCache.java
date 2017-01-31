package com.enablix.app.template.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.commons.caching.api.CachingService;
import com.enablix.services.util.template.TemplateWrapper;

@Component
public class TemplateCache {
	
	private static final String TEMPLATE_CACHE_KEY_PREFIX = "template.";

	@Autowired
	private CachingService cache;

	public TemplateCache() {
		super();
	}
	
	public TemplateWrapper getTemplate(String tenantId, String templateId) {
		return (TemplateWrapper) cache.get(tenantId, getCacheKey(templateId));
	}
	
	public void put(String tenantId, TemplateWrapper template) {
		cache.put(tenantId, getCacheKey(template.getTemplate().getId()), template);
	}
	
	private String getCacheKey(String templateId) {
		return TEMPLATE_CACHE_KEY_PREFIX + templateId;
	}
	
}
