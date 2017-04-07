package com.enablix.app.content.update;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.core.api.TemplateFacade;
import com.enablix.core.mongo.content.ContentCrudService;

@Component
public class InsertRootContainerHandler implements ContentUpdateHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(InsertRootContainerHandler.class);
	
	@Autowired
	private ContentCrudService crudService;
	
	@Override
	public Map<String, Object> updateContent(TemplateFacade template, String recordId, String contentQId,
			Map<String, Object> contentDataMap) {
		
		String collectionName = template.getCollectionName(contentQId);
		
		LOGGER.debug("Inserting new record in collection [{}]", collectionName);
		crudService.insert(collectionName, contentDataMap);
		
		return null;
	}


}
