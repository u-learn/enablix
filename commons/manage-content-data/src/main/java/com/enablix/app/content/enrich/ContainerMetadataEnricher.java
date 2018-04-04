package com.enablix.app.content.enrich;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.enablix.app.content.update.ContentUpdateContext;
import com.enablix.app.template.service.TemplateManager;
import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.commons.util.concurrent.Events;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.mongo.dao.GenericDao;
import com.enablix.core.mq.EventSubscription;

@Component
public class ContainerMetadataEnricher implements ContentEnricher {

	@Autowired
	private TemplateManager templateMgr;
	
	@Autowired
	private GenericDao dao;
	
	@Override
	public void enrich(ContentUpdateContext updateCtx, 
			Map<String, Object> content, TemplateFacade contentTemplate) {
		
		// add container name as a metadata in content
		ContainerType containerType = contentTemplate.getContainerDefinition(updateCtx.contentQId());
		
		content.put(ContentDataConstants.CONTAINER_NAME_METADATA_FLD, containerType.getLabel());
		
	}
	
	@EventSubscription(eventName = {Events.CONTAINER_LABEL_UPDATED})
	public void updateContainerMetadata(ContainerType event) {
		
		TemplateFacade template = templateMgr.getTemplateFacade(ProcessContext.get().getTemplateId());
		String collectionName = template.getCollectionName(event.getQualifiedId());
		
		Update update = new Update().set(ContentDataConstants.CONTAINER_NAME_METADATA_FLD, event.getLabel());
		Query query = Query.query(new Criteria());
		
		dao.updateMulti(query, update, collectionName);
	}

}
