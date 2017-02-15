package com.enablix.app.content.enrich;

import java.util.Map;

import com.enablix.app.content.update.ContentUpdateContext;
import com.enablix.services.util.template.TemplateWrapper;

public interface ContentEnricher {

	void enrich(ContentUpdateContext updateCtx, Map<String, Object> content, TemplateWrapper contentTemplate);
	
}
