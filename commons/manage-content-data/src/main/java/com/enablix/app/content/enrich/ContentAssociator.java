package com.enablix.app.content.enrich;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.content.association.ContentAssociationBuilder;
import com.enablix.app.content.association.ContentAssociationBuilderRegistry;
import com.enablix.app.content.update.ContentUpdateContext;
import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.core.domain.content.ContentAssociation;

@Component
public class ContentAssociator implements ContentEnricher {

	@Autowired
	private ContentAssociationBuilderRegistry builderRegistry;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void enrich(ContentUpdateContext updateCtx, Map<String, Object> content, ContentTemplate contentTemplate) {
		
		Collection<ContentAssociation> associations = new ArrayList<ContentAssociation>();
		
		for (ContentAssociationBuilder builder : builderRegistry.builders()) {
		
			Collection<ContentAssociation> assocs = builder.buildAssociations(
					contentTemplate, updateCtx, content);
			
			if (CollectionUtil.isNotEmpty(assocs)) {
				
				if (builder.replaceExisting()) {
					removeExistingAssociation(content, assocs);
				}
				
				associations.addAll(assocs);
			}
		}
		
		if (!associations.isEmpty()) {
			
			Map<String, Collection<ContentAssociation>> assocMap = null;
			
			Object associationData = content.get(ContentDataConstants.ASSOCIATIONS_KEY);
			if (associationData != null && associationData instanceof Map) {
				assocMap = (Map) associationData;
				
			} else {
				assocMap = new HashMap<String, Collection<ContentAssociation>>();
				content.put(ContentDataConstants.ASSOCIATIONS_KEY, assocMap);
			}
			
			for (ContentAssociation assoc : associations) {
				addAssociationToMap(assocMap, assoc);
			}
			
		}
		
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void removeExistingAssociation(Map<String, Object> content, Collection<ContentAssociation> assocs) {
		
		Object associationData = content.get(ContentDataConstants.ASSOCIATIONS_KEY);
		
		if (associationData != null && associationData instanceof Map) {
		
			Map<String, ?> assocMap = ((Map) associationData);
			for (ContentAssociation assoc : assocs) {
				assocMap.remove(assoc.getAssociationName());
			}
		}
	}
	
	private void addAssociationToMap(Map<String, Collection<ContentAssociation>> assocMap, 
			ContentAssociation assoc) {
		
		Collection<ContentAssociation> assocColl = assocMap.get(assoc.getAssociationName());
		
		if (assocColl == null) {
			assocColl = new ArrayList<ContentAssociation>();
			assocMap.put(assoc.getAssociationName(), assocColl);
		}
		
		assocColl.add(assoc);
	}

}
