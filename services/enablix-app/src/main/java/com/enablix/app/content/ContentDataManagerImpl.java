package com.enablix.app.content;

import java.util.Collection;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.content.enrich.ContentEnricher;
import com.enablix.app.content.enrich.ContentEnricherRegistry;
import com.enablix.app.content.update.ContentUpdateHandler;
import com.enablix.app.content.update.ContentUpdateHandlerFactory;
import com.enablix.app.content.update.UpdateContentRequest;
import com.enablix.app.content.update.UpdateContentRequestValidator;
import com.enablix.app.template.service.TemplateManager;
import com.enablix.commons.util.json.JsonUtil;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.core.mongo.content.ContentCrudService;

@Component
public class ContentDataManagerImpl implements ContentDataManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(ContentDataManagerImpl.class);
	
	@Autowired
	private TemplateManager templateMgr;
	
	@Autowired
	private UpdateContentRequestValidator updateValidator;

	@Autowired
	private ContentCrudService crud;
	
	@Autowired
	private ContentEnricherRegistry enricherRegistry;
	
	@Autowired
	private ContentUpdateHandlerFactory handlerFactory;
	
	/*
	 * Use cases:
	 * 1. Insert top level container i.e. new record == Mongo Insert
	 * 2. Insert sub-level container i.e. new data in existing record == Mongo $addToSet
	 * 		(http://docs.mongodb.org/manual/reference/operator/update/addToSet/)
	 * 3. Update existing container record attributes == Mongo update $set
	 */
	@Override
	public void saveData(UpdateContentRequest request) {
		
		Collection<Error> errors = updateValidator.validate(request);
		if (errors != null && !errors.isEmpty()) {
			LOGGER.error("Update request validation error: {}", errors);
			throw new IllegalArgumentException("Update request validation error: " + errors);
		}
		
		ContentUpdateHandler updateHandler = handlerFactory.getHandler(request);
		
		ContentTemplate template = templateMgr.getTemplate(request.getTemplateId());
		
		Map<String, Object> contentDataMap = JsonUtil.jsonToMap(request.getJsonData());
		
		// Enrichment step
		for (ContentEnricher enricher : enricherRegistry.getEnrichers()) {
			enricher.enrich(contentDataMap, template);
		}
		
		updateHandler.updateContent(template, request.getRecordId(), 
				request.getContentQId(), contentDataMap);
		
	}
	

	@Override
	public void deleteData(String templateId, String containerQId, String dataIdentity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String fetchDataJson(FetchContentRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

}
