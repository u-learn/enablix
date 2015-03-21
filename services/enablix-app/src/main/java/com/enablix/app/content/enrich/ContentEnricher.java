package com.enablix.app.content.enrich;

import java.util.Map;

import com.enablix.app.content.update.ContentUpdateContext;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;

public interface ContentEnricher {

	void enrich(ContentUpdateContext updateCtx, Map<String, Object> content, ContentTemplate contentTemplate);
	
}
