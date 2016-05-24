package com.enablix.app.content.enrich;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.content.ContentDataManager;
import com.enablix.app.content.label.DefaultContentLabelResolver;
import com.enablix.app.content.update.ContentUpdateContext;
import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.commons.util.QIdUtil;
import com.enablix.commons.util.StringUtil;
import com.enablix.core.api.Tag;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.core.mongo.content.ContentCrudService;
import com.enablix.services.util.TemplateUtil;

@Component
public class ContentHierarchyTagEnricher implements ContentEnricher {

	@Autowired
	private ContentCrudService crudService;
	
	@Autowired
	private ContentDataManager contentDataManager;
	
	private DefaultContentLabelResolver labelResolver = new DefaultContentLabelResolver();
	
	@Override
	public void enrich(ContentUpdateContext updateCtx, Map<String, Object> content, ContentTemplate contentTemplate) {
		
		List<Tag> tags = new ArrayList<>();
		
		if (!StringUtil.isEmpty(updateCtx.parentIdentity())) {
			addParentLabelAsTag(updateCtx, contentTemplate, tags);
		}
		
		if (!tags.isEmpty()) {
			content.put(ContentDataConstants.RECORD_TAGS_ATTR, tags);
		}
		
	}

	private void addParentLabelAsTag(ContentUpdateContext updateCtx, ContentTemplate contentTemplate, List<Tag> tags) {
		
		String parentCollection = TemplateUtil.findParentCollectionName(contentTemplate, updateCtx.contentQId());
		
		if (parentCollection != null) {
		
			Map<String, Object> parent = crudService.findRecord(parentCollection, updateCtx.parentIdentity());
			
			String parentQId = QIdUtil.getParentQId(updateCtx.contentQId());
			String parentLabel = labelResolver.findContentLabel(parent, contentTemplate, parentQId);
			
			Tag tag = new Tag(parentLabel);
			tags.add(tag);
			
			addParentLabelAsTag(contentTemplate, parentQId, parent, tags);
		}
	}
	
	private void addParentLabelAsTag(ContentTemplate template, String recordQId, Map<String, Object> record, List<Tag> tags) {
		
		Map<String, Object> parentRecord = contentDataManager.fetchParentRecord(template, recordQId, record);
		
		if (parentRecord != null) {
		
			String parentQId = QIdUtil.getParentQId(recordQId);
			String parentLabel = labelResolver.findContentLabel(parentRecord, template, parentQId);
			
			Tag tag = new Tag(parentLabel);
			tags.add(tag);
			
			addParentLabelAsTag(template, parentQId, parentRecord, tags);
		}
	}
	
}
