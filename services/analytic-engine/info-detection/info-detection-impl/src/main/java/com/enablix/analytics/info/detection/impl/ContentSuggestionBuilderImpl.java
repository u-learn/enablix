package com.enablix.analytics.info.detection.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.analytics.info.detection.Assessment;
import com.enablix.analytics.info.detection.ContentSuggestion;
import com.enablix.analytics.info.detection.ContentSuggestionBuilder;
import com.enablix.analytics.info.detection.LinkOpinion;
import com.enablix.analytics.info.detection.TypeAttrOpinion;
import com.enablix.analytics.info.detection.TypeOpinion;
import com.enablix.app.template.service.TemplateManager;
import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.api.ContentDataRecord;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.BoundedListDatastoreType;
import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.commons.xsdtopojo.ContentItemType;
import com.enablix.core.commons.xsdtopojo.DatastoreLocationType;
import com.enablix.services.util.TemplateUtil;

@Component
public class ContentSuggestionBuilderImpl implements ContentSuggestionBuilder {

	private static final Logger LOGGER = LoggerFactory.getLogger(ContentSuggestionBuilderImpl.class);
	
	private static final String CONTENT_SUGGESTION_BUILDER = "Content Suggestion Builder";
	
	@Autowired
	private TemplateManager templateMgr;

	@Override
	public List<ContentSuggestion> build(Assessment assessment) {
		
		String templateId = ProcessContext.get().getTemplateId();
		TemplateFacade template = templateMgr.getTemplateFacade(templateId);
		
		List<ContentSuggestion> contentSuggestions = new ArrayList<>();
		
		for (TypeOpinion typeOp : assessment.getTypeOpinions()) {
			
			String containerQId = typeOp.getContainerQId();
			ContainerType containerDef = template.getContainerDefinition(containerQId);
			
			if (containerDef != null) {

				Map<String, Object> data = new HashMap<>();
	
				// determine simple attributes
				for (Entry<String, List<TypeAttrOpinion>> attrOp : typeOp.getAttributes().entrySet()) {
					
					Object attrVal = determineAttrValue(attrOp.getKey(), attrOp.getValue());
					
					if (attrVal != null) {
						data.put(attrOp.getKey(), attrVal);
					}
				}
				
				// determine ref list attributes
				for (ContentItemType contentItem : containerDef.getContentItem()) {
					
					BoundedListDatastoreType refListDs = 
							TemplateUtil.checkAndGetBoundedRefListDatastore(contentItem);
					
					if (refListDs != null && refListDs.getLocation() == DatastoreLocationType.CONTENT) {
						
						Object attrVal = createLinkedOpinionAttrValue(assessment.getLinkOpinions(refListDs.getStoreId()));
						
						if (attrVal != null) {
							data.put(contentItem.getId(), attrVal);
						}
					}
				}
				
				
				ContentDataRecord record = new ContentDataRecord(templateId, containerQId, data);
				contentSuggestions.add(new ContentSuggestion(record, CONTENT_SUGGESTION_BUILDER, typeOp.getConfidence()));
				
			} else {
				LOGGER.warn("Container definition not found for container [{}]", containerQId);
			}
		}
		
		return contentSuggestions;
	}
	
	private Object createLinkedOpinionAttrValue(List<LinkOpinion> linkOpinions) {
		
		List<Map<String, Object>> attrValue = null;
		
		if (CollectionUtil.isNotEmpty(linkOpinions)) {
		
			attrValue = new ArrayList<>();
			
			for (LinkOpinion linkOp : linkOpinions) {
				
				Map<String, Object> linkRecordAttrs = new HashMap<>();
				linkRecordAttrs.put(ContentDataConstants.BOUNDED_ID_ATTR, linkOp.getLinkedRecord().getRecordIdentity());
				linkRecordAttrs.put(ContentDataConstants.BOUNDED_LABEL_ATTR, linkOp.getLinkedRecord().getTitle());
				
				if (!attrValue.contains(linkRecordAttrs)) {
					attrValue.add(linkRecordAttrs);
				}
			}
		}
			
		return attrValue;
	}

	protected Object determineAttrValue(String attributeId, List<TypeAttrOpinion> attrOps) {
		
		TypeAttrOpinion bestAttr = null;
		
		// find the best attr
		for (TypeAttrOpinion attrOp : attrOps) {
			
			if (bestAttr == null || attrOp.getConfidence() > bestAttr.getConfidence()) {
				bestAttr = attrOp;
			} 
		}
		
		return bestAttr == null ? null : bestAttr.getValue();
	}

}
