package com.enablix.app.template.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.commons.caching.api.CachingService;
import com.enablix.core.api.TemplateFacade;

@Component
public class TemplateCache {
	
	private static final String TEMPLATE_CACHE_KEY_PREFIX = "template.";

	@Autowired
	private CachingService cache;

	public TemplateCache() {
		super();
	}
	
	public TemplateFacade getTemplate(String tenantId, String templateId) {
		return (TemplateFacade) cache.get(tenantId, getCacheKey(templateId));
	}
	
	public void put(String tenantId, TemplateFacade template) {
		cache.put(tenantId, getCacheKey(template.getTemplate().getId()), template);
	}
	
	private String getCacheKey(String templateId) {
		return TEMPLATE_CACHE_KEY_PREFIX + templateId;
	}
	
}
