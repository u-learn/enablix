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

	public static ContentAssociation findParentAssociation(Map<String, Object> record) {
		
		ContentAssociation parentAssoc = null;
		
		if (record != null) {
			
			Object associations = record.get(ContentDataConstants.ASSOCIATIONS_KEY);
			
			if (associations != null && associations instanceof Map) {
			
				Map<?,?> mapAssoc = (Map<?,?>) associations;
				Object parentAssocList = mapAssoc.get(ContentDataConstants.PARENT_ASSOCIATION);
				
				if (parentAssocList != null && parentAssocList instanceof Collection) {
				
					Collection<?> parents = (Collection<?>) parentAssocList;
					if (!parents.isEmpty()) {
						parentAssoc = (ContentAssociation) parents.iterator().next();
					}
				}
			}
		}
		
		return parentAssoc;
	}

	public static String findPortalLabelAttribute(
			Map<String, Object> record, ContentTemplate template, String qId) {
		
		Object labelAttribute = null;
		String portalLabelAttributeId = TemplateUtil.getPortalLabelAttributeId(template, qId);
		ContainerType containerDef = TemplateUtil.findContainer(template.getDataDefinition(), qId);
		
		if (portalLabelAttributeId != null) {
			labelAttribute = record.get(portalLabelAttributeId);
		}
		
		return contentItemToString(labelAttribute, containerDef, portalLabelAttributeId);
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
