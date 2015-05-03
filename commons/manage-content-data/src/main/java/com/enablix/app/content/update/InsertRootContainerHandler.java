package com.enablix.app.content.update;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.core.mongo.content.ContentCrudService;
import com.enablix.services.util.TemplateUtil;

@Component
public class InsertRootContainerHandler implements ContentUpdateHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(InsertRootContainerHandler.class);
	
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
