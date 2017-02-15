package com.enablix.app.content.enrich;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.enablix.app.content.ContentDataManager;
import com.enablix.app.content.event.ContentDataSaveEvent;
import com.enablix.app.content.label.DefaultContentLabelResolver;
import com.enablix.app.content.update.ContentUpdateContext;
import com.enablix.app.content.update.UpdateContentAttributeHandler;
import com.enablix.app.template.service.TemplateManager;
import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.commons.util.QIdUtil;
import com.enablix.commons.util.StringUtil;
import com.enablix.commons.util.concurrent.Events;
import com.enablix.core.api.Tag;
import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.mongo.content.ContentCrudService;
import com.enablix.core.mq.EventSubscription;
import com.enablix.services.util.ContentDataUtil;
import com.enablix.services.util.TemplateUtil;
import com.enablix.services.util.template.TemplateWrapper;

@Component
public class ContentHierarchyTagEnricher implements ContentEnricher {

	@Autowired
	private ContentCrudService crudService;
	
	@Autowired
	private ContentDataManager contentDataManager;
	
	@Autowired
	private TemplateManager templateMgr;
	
	@Autowired
	private ContentCrudService crud;
	
	@Autowired
	private UpdateContentAttributeHandler updateContentHandler;
	
	private DefaultContentLabelResolver labelResolver = new DefaultContentLabelResolver();
	
	@Override
	public void enrich(ContentUpdateContext updateCtx, Map<String, Object> content, TemplateWrapper contentTemplate) {
		
		List<Tag> tags = new ArrayList<>();
		
		String parentIdentity = findParentIdentity(updateCtx, content);
		if (!StringUtil.isEmpty(parentIdentity)) {
			addParentLabelAsTag(updateCtx, parentIdentity, contentTemplate, tags);
		}
		
		if (!tags.isEmpty()) {
			content.put(ContentDataConstants.RECORD_TAGS_ATTR, tags);
		}
		
	}
	
	private String findParentIdentity(ContentUpdateContext updateCtx, Map<String, Object> content) {
		String parentIdentity = updateCtx.parentIdentity();
		return StringUtil.isEmpty(parentIdentity) ? ContentDataUtil.findParentIdentityFromAssociation(content) : parentIdentity;
	}

	private void addParentLabelAsTag(ContentUpdateContext updateCtx, String parentIdentity, 
			TemplateWrapper contentTemplate, List<Tag> tags) {
		
		String parentCollection = contentTemplate.getCollectionName(updateCtx.contentQId());
		
		if (parentCollection != null) {
		
			Map<String, Object> parent = crudService.findRecord(parentCollection, parentIdentity);
			
			resolveAndAddParentLabelAsTag(QIdUtil.getParentQId(updateCtx.contentQId()), contentTemplate, tags, parent);
		}
	}

	private void resolveAndAddParentLabelAsTag(String parentQId, TemplateWrapper contentTemplate,
			List<Tag> tags, Map<String, Object> parent) {
		
		String parentLabel = labelResolver.findContentLabel(parent, contentTemplate, parentQId);
		
		Tag tag = new Tag(parentLabel);
		tags.add(tag);
		
		findAndAddNextParentLabelAsTag(contentTemplate, parentQId, parent, tags);
	}
	
	private void findAndAddNextParentLabelAsTag(TemplateWrapper template, 
			String recordQId, Map<String, Object> record, List<Tag> tags) {
		
		Map<String, Object> parentRecord = contentDataManager.fetchParentRecord(template, recordQId, record);
		
		if (parentRecord != null) {
			String parentQId = QIdUtil.getParentQId(recordQId);
			resolveAndAddParentLabelAsTag(parentQId, template, tags, parentRecord);
		}
	}
	
	@EventSubscription(eventName = {Events.CONTENT_CHANGE_EVENT})
	public void updateHierarchyTagsInChildren(ContentDataSaveEvent event) {
		
		if (!event.isNewRecord()) {
		
			TemplateWrapper template = templateMgr.getTemplateWrapper(event.getTemplateId());
			
			String containerQId = event.getContainerType().getQualifiedId();
			
			String labelAttrId = template.getStudioLabelAttributeId(containerQId);
			
			// if there is change in the label attribute then we need to update the parent tags in children
			if (StringUtil.hasText(labelAttrId) && event.getChangeDelta().hasAttribute(labelAttrId)) {
				updateHierarchyTagOfChildren(event.getDataAsMap(), template, containerQId);
			}
		}
	}

	private void updateHierarchyTagOfChildren(
			Map<String, Object> parentRecord, TemplateWrapper template, String containerQId) {
		
		String recordIdentity = (String) parentRecord.get(ContentDataConstants.IDENTITY_KEY);
		ContainerType container = template.getContainerDefinition(containerQId);
		List<String> childContainerIds = TemplateUtil.getChildContainerIds(container);
		
		for (String childContainerId : childContainerIds) {
			
			String childQId = QIdUtil.createQualifiedId(containerQId, childContainerId);
			ContainerType childContainer = template.getContainerDefinition(childContainerId);
			
			if (TemplateUtil.hasOwnCollection(childContainer)) {
				
				String collName = template.getCollectionName(childQId);
				
				// find all children records and update their hierarchy tags
				
				int page = 0, size = 10;
				boolean hasMoreRecords = true;
				
				while (hasMoreRecords) {
					
					Pageable pageRequest = new PageRequest(page, size);
					Page<Map<String, Object>> childrenPage = 
							crud.findAllRecordWithParentId(collName, recordIdentity, pageRequest);
					
					if (childrenPage.hasContent()) {
						
						for (Map<String, Object> child : childrenPage.getContent()) {
						
							List<Tag> tags = new ArrayList<>();
							resolveAndAddParentLabelAsTag(containerQId, template, tags, parentRecord);
							
							if (!tags.isEmpty()) {
								child.put(ContentDataConstants.RECORD_TAGS_ATTR, tags);
							}
							
							updateContentHandler.updateContent(template, null, childQId, child);
							
							updateHierarchyTagOfChildren(child, template, childQId);
						}
						
					}
					
					hasMoreRecords = childrenPage.hasNext();
					page++;
				}
				
			}
		}
	}
	
}
