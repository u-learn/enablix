package com.enablix.app.content.enrich;

import java.util.Map;

import com.enablix.core.commons.xsdtopojo.ContentTemplate;

public interface ContentEnricher {

	void enrich(Map<String, Object> content, ContentTemplate contentTemplate);
	
}
