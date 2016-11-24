package com.enablix.services.util;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.commons.xsdtopojo.ContentItemClassType;
import com.enablix.core.commons.xsdtopojo.ContentItemType;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.core.domain.content.ContentAssociation;

public class ContentDataUtil {

	public static String findParentIdentityFromAssociation(Map<String, Object> record) {
		
		String parentIdentity = null;
		
		if (record != null) {
			
			Object associations = record.get(ContentDataConstants.ASSOCIATIONS_KEY);
			
			if (associations != null && associations instanceof Map) {
			
				Map<?,?> mapAssoc = (Map<?,?>) associations;
				Object parentAssocList = mapAssoc.get(ContentDataConstants.PARENT_ASSOCIATION);
				
				if (parentAssocList != null && parentAssocList instanceof Collection) {
				
					Collection<?> parents = (Collection<?>) parentAssocList;
					if (!parents.isEmpty()) {
						
						Object parentAssoc = parents.iterator().next();
						
						if (parentAssoc instanceof ContentAssociation) {
							parentIdentity = ((ContentAssociation) parentAssoc).getRecordIdentity();
							
						} else if (parentAssoc instanceof Map<?,?>) {
							parentIdentity = (String) ((Map<?,?>) parentAssoc).get(
											ContentDataConstants.RECORD_IDENTITY_KEY);
						}
					}
				}
			}
		}
		
		return parentIdentity;
	}

	public static String findPortalLabelValue(
			Map<String, Object> record, ContentTemplate template, String qId) {
		
		String portalLabelAttributeId = TemplateUtil.getPortalLabelAttributeId(template, qId);
		
		return findLabelAttributeValue(record, template, qId, portalLabelAttributeId);
	}
	
	public static String findStudioLabelValue(
			Map<String, Object> record, ContentTemplate template, String qId) {
		
		String studioLabelAttributeId = TemplateUtil.getStudioLabelAttributeId(template, qId);
		
		return findLabelAttributeValue(record, template, qId, studioLabelAttributeId);
	}
	
	public static String findLabelAttributeValue(Map<String, Object> record, 
			ContentTemplate template, String qId, String labelAttrId) {
		
		String labelAttributeValue = null;
		ContainerType containerDef = TemplateUtil.findContainer(template.getDataDefinition(), qId);
		
		if (labelAttrId != null) {
			
			Object labelAttribute = record.get(labelAttrId);
			
			if (TemplateUtil.isLinkedContainer(containerDef)) {
				containerDef = TemplateUtil.findContainer(template.getDataDefinition(), containerDef.getLinkContainerQId());
			}
			
			labelAttributeValue = contentItemToString(labelAttribute, containerDef, labelAttrId);
		}
		
		return labelAttributeValue == null ? containerDef.getLabel() : labelAttributeValue;
	}
	
	public static String findDocIdentity(Map<String, Object> record, ContentTemplate template, String qId) {
		
		String docIdentity = null;
		
		ContainerType containerDef = TemplateUtil.findContainer(template.getDataDefinition(), qId);
		
		for (ContentItemType itemType : containerDef.getContentItem()) {
			
			if (ContentItemClassType.DOC.equals(itemType.getType())) {
				
				Object doc = record.get(itemType.getId());
				if (doc != null && doc instanceof Map<?,?>) {
					Map<?,?> docDetails = (Map<?,?>) doc;
					docIdentity = String.valueOf(
							docDetails.get(ContentDataConstants.IDENTITY_KEY));
				}
				
				break;
			}
		}
		
		return docIdentity;
	}

	private static String contentItemToString(Object labelAttribute, ContainerType containerDef,
			String portalLabelAttributeId) {
		
		for (ContentItemType itemType : containerDef.getContentItem()) {
		
			if (itemType.getId().equals(portalLabelAttributeId)) {
				return ContentItemToStringTransformer.convert(labelAttribute, itemType);
			}
		}
		
		return null;
	}
	
	
	public static ContentDataRef contentDataToRef(
			Map<String, Object> contentData, ContentTemplate template, String containerQId) {
		
		Object identity = contentData.get(ContentDataConstants.IDENTITY_KEY);
		
		ContentDataRef contentDataRef = null;
		
		if (identity instanceof String) {
			String contentTitle = ContentDataUtil.findPortalLabelValue(contentData, template, containerQId);
			contentDataRef = new ContentDataRef(template.getId(), 
								containerQId, (String) identity, contentTitle);	
		} else {
			throw new IllegalStateException("[identity] not of type string. Found [" 
						+ identity.getClass().getName() + "]");	
		}
		
		return contentDataRef;
	}
	
	public static Date getContentCreatedAt(Map<String, Object> contentData) {
		return (Date) contentData.get(ContentDataConstants.CREATED_AT_KEY);
	}
	
	public static Date getContentModifiedAt(Map<String, Object> contentData) {
		return (Date) contentData.get(ContentDataConstants.MODIFIED_AT_KEY);
	}
	
}