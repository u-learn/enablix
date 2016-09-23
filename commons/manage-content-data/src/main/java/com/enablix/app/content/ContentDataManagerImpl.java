package com.enablix.app.content;

import java.util.ArrayList;
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
import com.enablix.app.content.event.ContentDataDelEvent;
import com.enablix.app.content.event.ContentDataEventListener;
import com.enablix.app.content.event.ContentDataEventListenerRegistry;
import com.enablix.app.content.event.ContentDataSaveEvent;
import com.enablix.app.content.fetch.FetchContentRequest;
import com.enablix.app.content.update.ContentUpdateHandler;
import com.enablix.app.content.update.ContentUpdateHandlerFactory;
import com.enablix.app.content.update.UpdateContentRequest;
import com.enablix.app.content.update.UpdateContentRequestValidator;
import com.enablix.app.template.service.TemplateManager;
import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.commons.util.QIdUtil;
import com.enablix.commons.util.StringUtil;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.commons.xsdtopojo.ContainerType;
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
	
	@Autowired
	private ContentDataEventListenerRegistry listenerRegistry;
	
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
		
		ContentTemplate template = templateMgr.getTemplate(request.getTemplateId());
		
		// check for linked container
		ContainerType container = TemplateUtil.findContainer(template.getDataDefinition(), request.getContentQId());
		
		String linkedContainerQId = container.getLinkContainerQId();
		if (!StringUtil.isEmpty(linkedContainerQId)) {
			request.setParentIdentity(null);
			request.setContentQId(linkedContainerQId);
			container = TemplateUtil.findContainer(template.getDataDefinition(), linkedContainerQId);
		}
		
		ContentUpdateHandler updateHandler = handlerFactory.getHandler(request);
		boolean newRecord = (request.isInsertRootRequest() || request.isInsertChildRequest()) 
				&& TemplateUtil.hasOwnCollection(template, request.getContentQId());
		
		Map<String, Object> contentDataMap = request.getDataAsMap();
		
		// Enrichment step
		for (ContentEnricher enricher : enricherRegistry.getEnrichers()) {
			enricher.enrich(request, contentDataMap, template);
		}
		
		updateHandler.updateContent(template, request.getParentIdentity(), 
				request.getContentQId(), contentDataMap);
		
		// notify listeners
		ContentDataSaveEvent saveEvent = new ContentDataSaveEvent(
				contentDataMap, request.getTemplateId(), container, newRecord);
		for (ContentDataEventListener listener : listenerRegistry.getListeners()) {
			listener.onContentDataSave(saveEvent);
		}
		
		return contentDataMap;
	}
	

	@Override
	public void deleteData(DeleteContentRequest request) {
		
		String contentQId = request.getContentQId();
		
		ContentTemplate template = templateMgr.getTemplate(request.getTemplateId());
		String collName = TemplateUtil.resolveCollectionName(template, contentQId);
		
		if (TemplateUtil.hasOwnCollection(template, contentQId)) {
			crud.deleteRecord(collName, request.getRecordIdentity());
			ContainerType containerType = TemplateUtil.findContainer(template.getDataDefinition(), contentQId);
			publishContentDeleteEvent(new ContentDataDelEvent(request.getTemplateId(), 
					request.getContentQId(), request.getRecordIdentity(), containerType));
			
		} else {
			String qIdRelativeToParent = QIdUtil.getElementId(contentQId);
			crud.deleteChild(collName, qIdRelativeToParent, request.getRecordIdentity());
		}
		
		deleteChildContainerData(template, contentQId, request.getRecordIdentity());
	}
	
	private void publishContentDeleteEvent(ContentDataDelEvent event) {
		for (ContentDataEventListener listener : listenerRegistry.getListeners()) {
			listener.onContentDataDelete(event);
		}
	}
	
	private void deleteChildContainerData(ContentTemplate template, String containerQId, String recordIdentity) {
		
		List<String> childContainerIds = TemplateUtil.getChildContainerIds(template, containerQId);
		for (String childContainerId : childContainerIds) {
			
			String childQId = QIdUtil.createQualifiedId(containerQId, childContainerId);
			
			if (TemplateUtil.hasOwnCollection(template, childQId)) {
				
				String collName = TemplateUtil.resolveCollectionName(template, childQId);
				List<String> deletedChildRecordIds = 
						crud.deleteRecordsWithParentId(collName, recordIdentity);
				
				ContainerType childContainer = TemplateUtil.findContainer(template.getDataDefinition(), childQId);
				
				for (String childRecordIdentity : deletedChildRecordIds) {

					publishContentDeleteEvent(new ContentDataDelEvent(template.getId(), 
							childQId, childRecordIdentity, childContainer));

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
		
		// check for linked container
		ContainerType container = TemplateUtil.findContainer(template.getDataDefinition(), contentQId);
		
		String linkedContainerQId = container.getLinkContainerQId();
		if (!StringUtil.isEmpty(linkedContainerQId)) {
			contentQId = linkedContainerQId;
		}
		
		String collName = TemplateUtil.resolveCollectionName(template, contentQId);
		String qIdRelativeToParent = TemplateUtil.getQIdRelativeToParentContainer(template, contentQId);
		
		if (!StringUtil.isEmpty(linkedContainerQId)) {
			
			if (!StringUtil.isEmpty(request.getParentRecordIdentity())) {
				
				data = request.getPageable() == null ? crud.findAllRecordWithLinkContainerId(collName, 
							container.getLinkContentItemId(), request.getParentRecordIdentity())
						: crud.findAllRecordWithLinkContainerId(collName, 
								container.getLinkContentItemId(), request.getParentRecordIdentity(), request.getPageable());
				
			} else if (!StringUtil.isEmpty(request.getRecordIdentity())) {
				// Fetch one record
				data = crud.findRecord(collName, qIdRelativeToParent, request.getRecordIdentity());
			} 				
			
			
		} else if (StringUtil.isEmpty(request.getParentRecordIdentity())
				&& StringUtil.isEmpty(request.getRecordIdentity())) {
			
			// Fetch all root elements for the template
			data = request.getPageable() == null ? crud.findAllRecord(collName) : 
						crud.findAllRecord(collName, request.getPageable());
			
		} else if (!StringUtil.isEmpty(request.getParentRecordIdentity())) {
			
			// Fetch all child containers
			if (TemplateUtil.hasOwnCollection(template, contentQId)) {
				// content is in its own collection, hence query with parent id
				data = request.getPageable() == null ? crud.findAllRecordWithParentId(collName, request.getParentRecordIdentity())
						: crud.findAllRecordWithParentId(collName, request.getParentRecordIdentity(), request.getPageable());
				
			} else {
				// content is a child array in parents collection, hence retrieve child elements
				data = request.getPageable() == null ? crud.findChildElements(collName, 
														qIdRelativeToParent, request.getParentRecordIdentity())
						: crud.findChildElements(collName, 
								qIdRelativeToParent, request.getParentRecordIdentity(), request.getPageable());
			}
			
		} else if (!StringUtil.isEmpty(request.getRecordIdentity())) {
			// Fetch one record
			data = crud.findRecord(collName, qIdRelativeToParent, request.getRecordIdentity());
		} 
		
		return data;
	}

	@Override
	public List<Map<String, Object>> fetchPeers(FetchContentRequest request) {
		
		String contentQId = request.getContentQId();
		
		ContentTemplate template = templateMgr.getTemplate(request.getTemplateId());
		String collName = TemplateUtil.resolveCollectionName(template, contentQId);
		String qIdRelativeToParent = TemplateUtil.getQIdRelativeToParentContainer(template, contentQId);

		List<Map<String, Object>> peers = new ArrayList<Map<String, Object>>();
		
		// Fetch one record
		Map<String, Object> recordData = 
				crud.findRecord(collName, qIdRelativeToParent, request.getRecordIdentity());
		
		if (recordData != null) {
			// Fetch all child containers
			if (TemplateUtil.hasOwnCollection(template, contentQId)) {
				
				String parentIdentity = ContentDataUtil.findParentIdentityFromAssociation(recordData);
				
				// content is in its own collection, hence query with parent id
				List<Map<String, Object>> allPeers = crud.findAllRecordWithParentId(
											collName, parentIdentity);
				
				// remove current
				for (Map<String, Object> item : allPeers) {
					String peerIdentity = (String) item.get(ContentDataConstants.IDENTITY_KEY);
					if (!request.getRecordIdentity().equals(peerIdentity)) {
						peers.add(item);
					}
				}
				
			} else {
				// TODO:
			}
		}
	
		return peers == null ? new ArrayList<Map<String, Object>>() : peers;
	}
	
	@Override
	public Map<String, Object> fetchParentRecord(ContentTemplate template, String recordQId, Map<String, Object> record) {
		
		Map<String, Object> parentRecord = null;
		
		String parentQId = QIdUtil.getParentQId(recordQId);
		
		String parentIdentity = ContentDataUtil.findParentIdentityFromAssociation(record);
		
		if (!StringUtil.isEmpty(parentIdentity)) {
		
			// check if data is self contained or in parent container
			String collName = TemplateUtil.resolveCollectionName(template, parentQId);
			
			if (TemplateUtil.hasOwnCollection(template, parentQId)) {
				parentRecord = crud.findRecord(collName, parentIdentity);
				
			} else {
				record = crud.findRecord(collName, QIdUtil.getElementId(parentQId), parentIdentity);
			}
		}
		
		return parentRecord;
	}


	@Override
	public Map<String, Object> getContentRecord(ContentDataRef dataRef, ContentTemplate template) {
		
		String collName = TemplateUtil.resolveCollectionName(template, dataRef.getContainerQId());
		Map<String, Object> triggerItemRecord = crud.findRecord(collName, dataRef.getInstanceIdentity());

		return triggerItemRecord;
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getContentRecords(String containerQId, List<String> recordIdentities,
			ContentTemplate template) {
		String collName = TemplateUtil.resolveCollectionName(template, containerQId);
		return crud.findRecords(collName, recordIdentities);
	}
	
}
