package com.enablix.app.content;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.enablix.app.content.enrich.ContentEnricher;
import com.enablix.app.content.enrich.ContentEnricherRegistry;
import com.enablix.app.content.event.ContentDataEventListener;
import com.enablix.app.content.event.ContentDataEventListenerRegistry;
import com.enablix.app.content.update.ContentUpdateHandler;
import com.enablix.app.content.update.ContentUpdateHandlerFactory;
import com.enablix.app.content.update.LinkageChange;
import com.enablix.app.content.update.UpdateContentRequest;
import com.enablix.app.template.service.TemplateManager;
import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.commons.util.StringUtil;
import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.core.api.ContentStackItem;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.content.event.ContentDataSaveEvent;
import com.enablix.core.domain.content.ContentChangeDelta;
import com.enablix.services.util.ContentDataUtil;
import com.enablix.services.util.TemplateUtil;

@Component
public class ContentPersistOperation {

	@Autowired
	private TemplateManager templateMgr;
	
	@Autowired
	private ContentEnricherRegistry enricherRegistry;
	
	@Autowired
	private ContentUpdateHandlerFactory handlerFactory;
	
	@Autowired
	private ContentDataEventListenerRegistry listenerRegistry;
	
	@Autowired
	private ContentChangeEvaluator contentChangeEvaluator;
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	public Map<String, Object> persist(UpdateContentRequest request) {
		
		TemplateFacade templateWrapper = templateMgr.getTemplateFacade(request.getTemplateId());
		
		// check for linked container
		ContainerType container = templateWrapper.getContainerDefinition(request.getContentQId()); 
		
		String linkedContainerQId = container.getLinkContainerQId();
		if (!StringUtil.isEmpty(linkedContainerQId)) {
			request.setParentIdentity(null);
			request.setContentQId(linkedContainerQId);
			container = templateWrapper.getContainerDefinition(linkedContainerQId);
		}
		
		// update the data store with content
		ContentUpdateHandler updateHandler = handlerFactory.getHandler(request);
		boolean newRecord = (request.isInsertRootRequest() || request.isInsertChildRequest()) 
				&& TemplateUtil.hasOwnCollection(container);
		
		Map<String, Object> contentDataMap = request.getDataAsMap();
		
		// Enrichment step
		for (ContentEnricher enricher : enricherRegistry.getEnrichers()) {
			enricher.enrich(request, contentDataMap, templateWrapper);
		}
		
		Map<String, Object> oldRecord = updateHandler.updateContent(templateWrapper, request.getParentIdentity(), 
				request.getContentQId(), contentDataMap);
		
		// notify listeners
		ContentDataSaveEvent saveEvent = new ContentDataSaveEvent(
				contentDataMap, request.getTemplateId(), container, newRecord);
		saveEvent.setPriorData(oldRecord);
		
		ContentChangeDelta delta = contentChangeEvaluator.findDelta(saveEvent.getPriorData(), 
				saveEvent.getDataAsMap(), saveEvent.getContainerType());
		saveEvent.setChangeDelta(delta);
		
		for (ContentDataEventListener listener : listenerRegistry.getListeners()) {
			listener.onContentDataSave(saveEvent);
		}
		
		// check for linkage change
		if (CollectionUtil.isNotEmpty(request.getLinkageChanges())) {
			processLinkageChange(contentDataMap, container, templateWrapper, request);
		}
		
		return contentDataMap;
	}

	private void processLinkageChange(Map<String, Object> contentDataMap, ContainerType container,
			TemplateFacade template, UpdateContentRequest request) {

		List<LinkageChange> linkageChange = request.getLinkageChanges();
		
		for (LinkageChange change : linkageChange) {
			
			ContainerType linkContainer = template.getContainerDefinition(change.getLinkContainerQId());
			
			if (linkContainer != null) {
				// process removed links 
				processRemovedLinks(linkContainer, change.getRemovedLinks(), contentDataMap, container, template);
				
				// process new links
				processNewLinks(linkContainer, change.getNewLinks(), contentDataMap, container, template);
			}
		}

	}

	private void processNewLinks(ContainerType linkContainer, List<ContentStackItem> newLinks,
			Map<String, Object> contentDataMap, ContainerType container, TemplateFacade template) {
		
		String collectionName = template.getCollectionName(linkContainer.getLinkContainerQId());
		
		if (StringUtil.hasText(collectionName)) {
			// insert new value in linked containers content item
			String recordIdentity = ContentDataUtil.getRecordIdentity(contentDataMap);
			String recordTitle = ContentDataUtil.getRecordTitle(contentDataMap);
			
			Map<String, String> updateVal = new HashMap<>();
			updateVal.put(ContentDataConstants.ID_FLD_KEY, recordIdentity);
			updateVal.put(ContentDataConstants.LBL_FLD_KEY, recordTitle);
			
			Update update = new Update();
			update.addToSet(linkContainer.getLinkContentItemId(), updateVal);
			
			List<String> lnkIdentities = newLinks.stream().map(ContentStackItem::getIdentity).collect(Collectors.toList());
			String existingLinkCheckFieldId = linkContainer.getLinkContentItemId() + "." + ContentDataConstants.ID_FLD_KEY;
			
			Query query = Query.query(Criteria.where(ContentDataConstants.IDENTITY_KEY).in(lnkIdentities)
											  .and(existingLinkCheckFieldId).ne(recordIdentity));
			mongoTemplate.updateMulti(query, update, collectionName);
		}
		
	}

	private void processRemovedLinks(ContainerType linkContainer, List<ContentStackItem> removedLinks,
			Map<String, Object> contentDataMap, ContainerType container, TemplateFacade template) {
		
		String collectionName = template.getCollectionName(linkContainer.getLinkContainerQId());
		
		if (StringUtil.hasText(collectionName)) {
			// remove link from the link container
			String recordIdentity = ContentDataUtil.getRecordIdentity(contentDataMap);
			
			Update update = new Update();
			update.pull(linkContainer.getLinkContentItemId(), 
						Query.query(Criteria.where(ContentDataConstants.ID_FLD_KEY).is(recordIdentity)));
			
			List<String> lnkIdentities = removedLinks.stream().map(ContentStackItem::getIdentity).collect(Collectors.toList());
			Query query = Query.query(Criteria.where(ContentDataConstants.IDENTITY_KEY).in(lnkIdentities));
			
			mongoTemplate.updateMulti(query, update, collectionName);
		}
		
	}
	
}
