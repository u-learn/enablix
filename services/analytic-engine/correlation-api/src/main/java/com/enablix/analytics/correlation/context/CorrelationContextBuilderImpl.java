package com.enablix.analytics.correlation.context;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.content.ContentDataManager;
import com.enablix.app.template.service.TemplateManager;
import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.content.connection.repo.ContentTypeConnectionRepository;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.commons.xsdtopojo.BoundedType;
import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.commons.xsdtopojo.ContentItemClassType;
import com.enablix.core.commons.xsdtopojo.ContentItemType;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.core.domain.content.connection.ContentTypeConnection;
import com.enablix.core.domain.content.connection.ContentValueConnection;
import com.enablix.services.util.TemplateUtil;

@Component
public class CorrelationContextBuilderImpl implements CorrelationContextBuilder {

	@Autowired
	private TemplateManager templateMgr;
	
	@Autowired
	private ContentTypeConnectionRepository contentConnRepo;
	
	@Autowired
	private ContentDataManager contentDataMgr;
	
	@Override
	public CorrelationContext buildContext(ContentDataRef item) {
		
		CorrelationContext context = new CorrelationContext();
		
		// set the content template in the context
		ContentTemplate template = templateMgr.getTemplate(ProcessContext.get().getTemplateId());
		context.setTemplate(template);
		
		// find the content connections applicable based on containerQId
		Map<String, Object> contentRecord = contentDataMgr.getContentRecord(item, template);
		ContainerType itemContainer = TemplateUtil.findContainer(template.getDataDefinition(), item.getContainerQId());
		
		List<ContentTypeConnection> matchedConnections = contentConnRepo.findByHoldingContainers(item.getContainerQId());
		for (ContentTypeConnection contentConn : matchedConnections) {
			context.getContainersInScope().addAll(
					findContainersInScope(contentConn, contentRecord, template, itemContainer));
		}
		
		return context;
	}

	private Set<String> findContainersInScope(ContentTypeConnection contentConn, 
			Map<String, Object> contentRecord, ContentTemplate template, ContainerType itemContainer) {
		
		Set<String> inScopeContainers = new HashSet<>();
		
		ContainerType contentConnContainer = TemplateUtil.findContainer(
				template.getDataDefinition(), contentConn.getContentQId());
		
		for (ContentItemType contentItem : itemContainer.getContentItem()) {
			
			Object contentRecordValue = contentRecord.get(contentItem.getId());
			
			if (contentRecordValue != null) {
				
				if (contentConnContainer != null) {
					
					if (contentItem.getType() == ContentItemClassType.BOUNDED) {
						matchAndAddConnectedContainersForBoundedContentItem(
								contentConn, inScopeContainers, contentConnContainer,
								contentItem, contentRecordValue);
					}
					
				} else if (contentItem.getQualifiedId().equals(contentConn.getContentQId())) {
					
					if (contentRecordValue != null) {
						findAndAddConnectedContainers(contentConn, inScopeContainers, contentRecordValue);
					}
					
				} 
			}
		}
		
		return inScopeContainers;
	}

	@SuppressWarnings("unchecked")
	private void matchAndAddConnectedContainersForBoundedContentItem(
			ContentTypeConnection contentConn, Set<String> inScopeContainers,
			ContainerType contentConnContainer, ContentItemType contentItem, Object contentRecordValue) {
		
		BoundedType boundedItem = contentItem.getBounded();
		
		if (boundedItem.getRefList() != null) {
		
			if (contentConnContainer.getId().equals(
					boundedItem.getRefList().getDatastore().getStoreId())) {
				
				if (contentRecordValue instanceof List) {
					
					List<Map<String, Object>> contentRecListVal = (List<Map<String, Object>>) contentRecordValue;
					
					for (Map<String, Object> refVal : contentRecListVal) {
						findAndAddConnectedContainers(contentConn, inScopeContainers, 
								refVal.get(ContentDataConstants.BOUNDED_ID_ATTR));
					}
				}
				
			}
			 
		}
	}

	private void findAndAddConnectedContainers(ContentTypeConnection contentConn, 
			Set<String> inScopeContainers, Object contentRecordValue) {
		
		if (contentRecordValue != null) {
		
			for (ContentValueConnection contentConnVal : contentConn.getConnections()) {
				if (contentConnVal.getContentValue().equals(contentRecordValue)) {
					inScopeContainers.addAll(contentConnVal.getConnectedContainers());
				}
			}
		}
	}

}
