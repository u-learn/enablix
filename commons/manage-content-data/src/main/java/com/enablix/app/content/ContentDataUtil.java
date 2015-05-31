package com.enablix.app.content;

import java.util.Collection;
import java.util.Map;

import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.commons.xsdtopojo.ContentItemType;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.core.domain.content.ContentAssociation;
import com.enablix.services.util.TemplateUtil;

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
			labelAttributeValue = contentItemToString(labelAttribute, containerDef, labelAttrId);
		}
		
		return labelAttributeValue;
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
	
	
}
