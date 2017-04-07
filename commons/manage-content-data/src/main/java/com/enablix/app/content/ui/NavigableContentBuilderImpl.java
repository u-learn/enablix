package com.enablix.app.content.ui;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.content.label.ContentLabelResolver;
import com.enablix.app.template.service.TemplateManager;
import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.commons.util.QIdUtil;
import com.enablix.commons.util.StringUtil;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.mongo.content.ContentCrudService;
import com.enablix.core.mongo.view.MongoDataView;
import com.enablix.services.util.ContentDataUtil;
import com.enablix.services.util.TemplateUtil;

@Component
public class NavigableContentBuilderImpl implements NavigableContentBuilder {

	@Autowired
	private ContentCrudService crudService;
	
	@Autowired
	private TemplateManager templateMgr;
	
	@Override
	public NavigableContent build(ContentDataRef data, ContentLabelResolver labelResolver) {
		TemplateFacade template = templateMgr.getTemplateFacade(data.getTemplateId());
		return buildNavigableContent(template, data.getContainerQId(), data.getInstanceIdentity(), null);
	}
	
	private NavigableContent buildNavigableContent(TemplateFacade template, 
			String qId, String identity, NavigableContent child) {

		NavigableContent navigableContent = null;
		
		ContainerType container = template.getContainerDefinition(qId);
		
		// check if data is self contained or in parent container
		String collName = template.getCollectionName(qId);
		
		if (StringUtil.hasText(collName)) {
			Map<String, Object> record = null;
			
			if (TemplateUtil.hasOwnCollection(container)) {
				record = crudService.findRecord(collName, identity, MongoDataView.ALL_DATA);
				
			} else {
				record = crudService.findRecord(collName, QIdUtil.getElementId(qId), identity, MongoDataView.ALL_DATA);
			}
			
			navigableContent = buildNavigableContent(template, container, child, record);
		}
		
		return navigableContent;
	}

	private NavigableContent buildNavigableContent(
			TemplateFacade template, ContainerType container, NavigableContent child,
			Map<String, Object> record) {
		
		NavigableContent navigableContent = null;

		if (record != null) {
			
			navigableContent = createNavigableContent(record, container, template, child);
			
			String parentIdentity = ContentDataUtil.findParentIdentityFromAssociation(record);
			
			if (parentIdentity != null) {
				navigableContent = buildNavigableContent(template, 
						QIdUtil.getParentQId(container.getQualifiedId()), 
						parentIdentity, navigableContent);
			}
		}
		
		return navigableContent;
	}
	
	private NavigableContent createNavigableContent(Map<String, Object> record, 
			ContainerType container, TemplateFacade template, NavigableContent child) {
		
		String containerQId = container.getQualifiedId();
		String label = ContentDataUtil.findPortalLabelValue(record, template, containerQId);
		
		NavigableContent content = new NavigableContent(containerQId, 
				(String) record.get(ContentDataConstants.IDENTITY_KEY), label, container.getLabel());
		
		Map<String, Object> docDetails = ContentDataUtil.findDocRecord(record, container);
		if (docDetails != null) {
			String docIdentity = (String) docDetails.get(ContentDataConstants.IDENTITY_KEY);
			content.setDocIdentity(docIdentity);
			content.setDocDetails(docDetails);
		}

		content.setNext(child);
		
		return content;
	}

	@Override
	public NavigableContent build(Map<String, Object> record, String templateId, 
			String qId, ContentLabelResolver labelResolver) {
		TemplateFacade template = templateMgr.getTemplateFacade(templateId);
		return buildNavigableContent(template, template.getContainerDefinition(qId), null, record);
	}
	

}
