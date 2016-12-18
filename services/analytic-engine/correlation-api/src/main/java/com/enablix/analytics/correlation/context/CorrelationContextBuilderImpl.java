package com.enablix.analytics.correlation.context;

import java.util.Collection;
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
import com.enablix.core.domain.content.connection.ConnectionContextAttribute;
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
		
		// find and match content type connections
		List<ContentTypeConnection> matchedConnections = contentConnRepo.findByHoldingContainers(item.getContainerQId());

		for (ContentTypeConnection contentConn : matchedConnections) {
			
			if (matchConnectionContextWithRecord(contentConn, contentRecord, template, itemContainer)) {
				context.getFilteredContainerCategories().add(contentConn.getConnectedContainerCategory());
				context.getContainersInScope().addAll(
					findContainersInScope(contentConn, contentRecord, template, itemContainer));
			}
		}
		
		return context;
	}

	private boolean matchConnectionContextWithRecord(ContentTypeConnection contentConn,
			Map<String, Object> contentRecord, ContentTemplate template, ContainerType recordContainer) {
		
		if (contentConn.getConnectionContext() != null) {
			// check if all of the connection context attribute match the content record
			// if any attribute is not present in the current record, then it is assumed to have matched
			for (ConnectionContextAttribute attr : contentConn.getConnectionContext().getContextAttributes()) {
				
				if (isNotNullOrEmptyCollection(attr.getAttributeValue()) && 
						!matchConnContextAttrWithRecord(attr, contentRecord, template, recordContainer)) {
					return false;
				}
			}
		}
		
		return true;
	}

	private boolean isNotNullOrEmptyCollection(Object attrVal) {
		return attrVal != null && (attrVal instanceof Collection ? !((Collection<?>) attrVal).isEmpty() : true);
	}

	private boolean matchConnContextAttrWithRecord(ConnectionContextAttribute attr, 
			Map<String, Object> contentRecord, ContentTemplate template, ContainerType recordContainer) {
		
		return recordHasSubContent(attr.getAttributeQId(), attr.getAttributeValue(), 
				contentRecord, template, recordContainer, true);
	}
	
	/*
	 * Check if the given content record has an attribute matching the <code>subContentQId</code> passed in
	 * as an argument and if present, does the value matches the given value of sub-content 
	 */
	private boolean recordHasSubContent(String subContentQId, Object subContentValue, Map<String, Object> contentRecord,
			ContentTemplate template, ContainerType recordContainer, boolean defaultMatchValue) {
		
		ContainerType subContentContainer = TemplateUtil.findContainer(template.getDataDefinition(), subContentQId);
		
		// Traverse through each attribute-type for the content record;
		// and match the attribute type and value in the content record with
		// values passed in the method
		for (ContentItemType contentItem : recordContainer.getContentItem()) {
			
			Object contentRecordValue = contentRecord.get(contentItem.getId());
			
			// if sub-content is a container type, then check if the content record attribute is
			// a bounded list (i.e. drop-down value) and match with it
			if (subContentContainer != null) {
				
				if (contentItem.getType() == ContentItemClassType.BOUNDED
						&& matchBoundedDatastoreWithSubContent(subContentContainer, contentItem.getBounded())) {
					
					return isNotNullOrEmptyCollection(contentRecordValue) && 
								matchSubContentWithBoundedContentItem(subContentContainer, subContentValue,
									contentItem.getBounded(), contentRecordValue, defaultMatchValue);
				}
				
			} else if (contentItem.getQualifiedId().equals(subContentQId)) {
				return subContentValue.equals(contentRecordValue);
			}
			
		}
		
		return defaultMatchValue;
	}
	
	@SuppressWarnings("unchecked")
	private boolean matchSubContentWithBoundedContentItem(
			ContainerType subContentContainer, Object subContentValue, BoundedType recordBoundedType, 
			Object recordBoundedItemValue, boolean defaultMatchValue) {
		
		if (recordBoundedType.getRefList() != null) {
		
			// first check that the sub-content that we are looking for matches the
			// data store id configured for this bounded type attribute
			if (matchBoundedDatastoreWithSubContent(subContentContainer, recordBoundedType)) {
				
				if (recordBoundedItemValue instanceof List) {
					
					List<Map<String, Object>> contentRecListVal = (List<Map<String, Object>>) recordBoundedItemValue;
					
					// now check that the value in the record matches the value of sub-content
					if (subContentValue instanceof String) {

						String strSubContentVal = (String) subContentValue;
						
						return matchSubContentValWithRecordValue(contentRecListVal, strSubContentVal);
						
					} else if (subContentValue instanceof List) {
						// sub-content value to match is also a list then there needs to be one common value between
						// the two list items to call it a match
						List<Map<String, Object>> subContentListVal = (List<Map<String, Object>>) subContentValue;
						
						for (Map<String, Object> subContentListItem : subContentListVal) {
						
							if (matchSubContentValWithRecordValue(contentRecListVal, 
									(String) subContentListItem.get(ContentDataConstants.BOUNDED_ID_ATTR))) {
								return true;
							}
						}
						
						return false;
					}
				}
			}
		}
		
		return defaultMatchValue;
	}

	private boolean matchBoundedDatastoreWithSubContent(ContainerType subContentContainer,
			BoundedType recordBoundedType) {
		return recordBoundedType.getRefList() != null && subContentContainer.getId().equals(
				recordBoundedType.getRefList().getDatastore().getStoreId());
	}
	

	private boolean matchSubContentValWithRecordValue(List<Map<String, Object>> contentRecListVal,
			String strSubContentVal) {
		
		for (Map<String, Object> refVal : contentRecListVal) {
			if (strSubContentVal.equals(refVal.get(ContentDataConstants.BOUNDED_ID_ATTR))) {
				return true;
			}
		}
		
		return false;
	}

	private Set<String> findContainersInScope(ContentTypeConnection contentConn, 
			Map<String, Object> contentRecord, ContentTemplate template, ContainerType itemContainer) {
		
		Set<String> inScopeContainers = new HashSet<>();
		
		for (ContentValueConnection valueConn : contentConn.getConnections()) {
		
			if (recordHasSubContent(contentConn.getContentQId(), valueConn.getContentValue(), 
					contentRecord, template, itemContainer, false)) {
				inScopeContainers.addAll(valueConn.getConnectedContainers());
			}
		}
		
		return inScopeContainers;
	}

}
