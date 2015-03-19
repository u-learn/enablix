package com.enablix.app.content.update;

import java.util.Map;

import com.enablix.core.commons.xsdtopojo.ContentTemplate;


public interface ContentUpdateHandler {

	void updateContent(ContentTemplate template, String recordId, 
			String contentQId, Map<String, Object> contentDataMap);
	
}
