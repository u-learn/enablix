package com.enablix.app.content;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.content.delete.DeleteContentRequest;
import com.enablix.app.content.enrich.ContentEnricher;
import com.enablix.app.content.enrich.ContentEnricherRegistry;
import com.enablix.app.content.fetch.FetchContentRequest;
import com.enablix.app.content.update.ContentUpdateHandler;
import com.enablix.app.content.update.ContentUpdateHandlerFactory;
import com.enablix.app.content.update.UpdateContentRequest;
import com.enablix.app.content.update.UpdateContentRequestValidator;
import com.enablix.app.template.service.TemplateManager;
import com.enablix.commons.util.QIdUtil;
import com.enablix.commons.util.StringUtil;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.core.mongo.content.ContentCrudService;
import com.enablix.services.util.TemplateUtil;

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
	public Map<String, Object> saveData(UpdateContentRequest request) {
		
		Collection<Error> errors = updateValidator.validate(request);
		if (errors != null && !errors.isEmpty()) {
			LOGGER.error("Update request validation error: {}", errors);
			throw new IllegalArgumentException("Update request validation error: " + errors);
		}
		
		ContentUpdateHandler updateHandler = handlerFactory.getHandler(request);
		
		ContentTemplate template = templateMgr.getTemplate(request.getTemplateId());
		
		Map<String, Object> contentDataMap = request.getDataAsMap();
		
		// Enrichment step
		for (ContentEnricher enricher : enricherRegistry.getEnrichers()) {
			enricher.enrich(request, contentDataMap, template);
		}
		
		updateHandler.updateContent(template, request.getParentIdentity(), 
				request.getContentQId(), contentDataMap);
		
		return contentDataMap;
		
	}
	

	@Override
	public void deleteData(DeleteContentRequest request) {
		
		String contentQId = request.getContentQId();
		
		ContentTemplate template = templateMgr.getTemplate(request.getTemplateId());
		String collName = TemplateUtil.resolveCollectionName(template, contentQId);
		
		if (TemplateUtil.hasOwnCollection(template, contentQId)) {
			crud.deleteRecord(collName, request.getRecordIdentity());
			
		} else {
			String qIdRelativeToParent = QIdUtil.getElementId(contentQId);
			crud.deleteChild(collName, qIdRelativeToParent, request.getRecordIdentity());
		}
		
		deleteChildContainerData(template, contentQId, request.getRecordIdentity());
	}
	
	private void deleteChildContainerData(ContentTemplate template, String containerQId, String recordIdentity) {
		
		List<String> childContainerIds = TemplateUtil.getChildContainerIds(template, containerQId);
		for (String childContainerId : childContainerIds) {
			
			String childQId = QIdUtil.createQualifiedId(containerQId, childContainerId);
			
			if (TemplateUtil.hasOwnCollection(template, childQId)) {
				
				String collName = TemplateUtil.resolveCollectionName(template, childQId);
				List<String> deletedChildRecordIds = 
						crud.deleteRecordsWithParentId(collName, recordIdentity);
				
				for (String childRecordIdentity : deletedChildRecordIds) {
					deleteChildContainerData(template, childQId, childRecordIdentity);
				}
			}
		}
		
	}

	@Override
	public Object fetchDataJson(FetchContentRequest request) {
		
		Object data = null;
		
		String contentQId = request.getContentQId();
		
		ContentTemplate template = templateMgr.getTemplate(request.getTemplateId());
		String collName = TemplateUtil.resolveCollectionName(template, contentQId);
		String qIdRelativeToParent = TemplateUtil.getQIdRelativeToParentContainer(template, contentQId);
		
		if (StringUtil.isEmpty(request.getParentRecordIdentity())
				&& StringUtil.isEmpty(request.getRecordIdentity())) {
			
			// Fetch all root elements for the template
			data = crud.findAllRecord(collName);
			
		} else if (!StringUtil.isEmpty(request.getParentRecordIdentity())) {
			
			// Fetch all child containers
			if (TemplateUtil.hasOwnCollection(template, contentQId)) {
				// content is in its own collection, hence query with parent id
				data = crud.findAllRecordWithParentId(collName, request.getParentRecordIdentity());
				
			} else {
				// content is a child array in parents collection, hence retrieve child elements
				data = crud.findChildElements(collName, 
						qIdRelativeToParent, request.getParentRecordIdentity());
			}
			
		} else if (!StringUtil.isEmpty(request.getRecordIdentity())) {
			// Fetch one record
			data = crud.findRecord(collName, qIdRelativeToParent, request.getRecordIdentity());
		} 
		
		return data;
	}

}
