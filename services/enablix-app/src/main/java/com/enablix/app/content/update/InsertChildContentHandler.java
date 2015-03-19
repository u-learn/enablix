package com.enablix.app.content.update;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.content.TemplateUtil;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.core.mongo.content.ContentCrudService;

@Component
public class InsertChildContentHandler implements ContentUpdateHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(InsertChildContentHandler.class);
	
	
	@Autowired
	private ContentCrudService crudService;
	
	@Override
	public void updateContent(ContentTemplate template, String recordId, String contentQId,
			Map<String, Object> contentDataMap) {
		
		String collectionName = TemplateUtil.resolveCollectionName(template, contentQId);
		
		LOGGER.debug("Inserting new record in collection [{}]", collectionName);
		crudService.insert(collectionName, contentDataMap);
		
	}

}
