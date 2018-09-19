package com.enablix.analytics.info.detection.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.enablix.analytics.info.detection.Assessment;
import com.enablix.analytics.info.detection.ContentSuggestion;
import com.enablix.analytics.info.detection.ContentSuggestionBuilder;
import com.enablix.analytics.info.detection.InfoDetectionContext;
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
import com.enablix.services.util.ContentDataUtil;
import com.enablix.services.util.TemplateUtil;

public class ContentSuggestionBuilderImpl implements ContentSuggestionBuilder {

	private static final Logger LOGGER = LoggerFactory.getLogger(ContentSuggestionBuilderImpl.class);
	
	private static final String CONTENT_SUGGESTION_BUILDER = "Content Suggestion Builder";
	
	@Autowired
	private TemplateManager templateMgr;

	@Override
	public List<ContentSuggestion> build(InfoDetectionContext ctx) {
		
		Assessment assessment = ctx.getAssessment();
		
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
				
				checkAndRetainExistingAttributes(data, typeOp, ctx);
				
				ContentDataRecord record = new ContentDataRecord(templateId, containerQId, data);
				contentSuggestions.add(new ContentSuggestion(record, CONTENT_SUGGESTION_BUILDER, typeOp.getConfidence()));
				
			} else {
				LOGGER.warn("Container definition not found for container [{}]", containerQId);
			}
		}
		
		return contentSuggestions;
	}
	
	private void checkAndRetainExistingAttributes(Map<String, Object> data, TypeOpinion typeOp, 
			InfoDetectionContext ctx) {
		
		Map<String, Object> existingRecord = typeOp.getExistingRecord();
		
		if (CollectionUtil.isNotEmpty(existingRecord)) {
			
			data.put(ContentDataConstants.IDENTITY_KEY, ContentDataUtil.getRecordIdentity(existingRecord));
			data.put(ContentDataConstants.CREATED_AT_KEY, ContentDataUtil.getContentCreatedAt(existingRecord));
			data.put(ContentDataConstants.CREATED_BY_KEY, ContentDataUtil.getContentCreatedBy(existingRecord));
			data.put(ContentDataConstants.CREATED_BY_NAME_KEY, ContentDataUtil.getContentCreatedByName(existingRecord));
			data.put(ContentDataConstants.MODIFIED_AT_KEY, ContentDataUtil.getContentModifiedAt(existingRecord));
			data.put(ContentDataConstants.MODIFIED_BY_KEY, ContentDataUtil.getContentModifiedBy(existingRecord));
			data.put(ContentDataConstants.MODIFIED_BY_NAME_KEY, ContentDataUtil.getContentModifiedByName(existingRecord));
			
			List<String> attrs = ctx.getInfoDetectionConfig().retainExistingAttrOnUpdate(typeOp.getContainerQId());
			if (CollectionUtil.isNotEmpty(attrs)) {
				attrs.forEach((attr) -> data.put(attr, existingRecord.get(attr)));
			}
		}
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
