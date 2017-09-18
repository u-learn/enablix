package com.enablix.app.content;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.content.enrich.ContentEnricher;
import com.enablix.app.content.enrich.ContentEnricherRegistry;
import com.enablix.app.content.event.ContentDataEventListener;
import com.enablix.app.content.event.ContentDataEventListenerRegistry;
import com.enablix.app.content.update.ContentUpdateHandler;
import com.enablix.app.content.update.ContentUpdateHandlerFactory;
import com.enablix.app.content.update.UpdateContentRequest;
import com.enablix.app.template.service.TemplateManager;
import com.enablix.commons.util.StringUtil;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.content.event.ContentDataSaveEvent;
import com.enablix.core.domain.content.ContentChangeDelta;
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
		
		return contentDataMap;
	}
	
}
