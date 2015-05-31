package com.enablix.app.content.ui;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.content.ContentDataUtil;
import com.enablix.app.content.label.ContentLabelResolver;
import com.enablix.app.template.service.TemplateManager;
import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.commons.util.QIdUtil;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.core.mongo.content.ContentCrudService;
import com.enablix.services.util.TemplateUtil;

@Component
public class NavigableContentBuilderImpl implements NavigableContentBuilder {

	@Autowired
	private ContentCrudService crudService;
	
	@Autowired
	private TemplateManager templateMgr;
	
	@Override
	public NavigableContent build(ContentDataRef data, ContentLabelResolver labelResolver) {
		ContentTemplate template = templateMgr.getTemplate(data.getTemplateId());
		return buildNavigableContent(template, data.getContainerQId(), data.getInstanceIdentity(), null);
	}
	
	private NavigableContent buildNavigableContent(ContentTemplate template, 
			String qId, String identity, NavigableContent child) {

		NavigableContent navigableContent = null;
		
		// check if data is self contained or in parent container
		String collName = TemplateUtil.resolveCollectionName(template, qId);
		
		Map<String, Object> record = null;
		
		if (TemplateUtil.hasOwnCollection(template, qId)) {
			record = crudService.findRecord(collName, identity);
			
		} else {
			record = crudService.findRecord(collName, QIdUtil.getElementId(qId), identity);
		}
		
		if (record != null) {
			
			navigableContent = createNavigableContent(record, qId, template, child);
			
			String parentIdentity = ContentDataUtil.findParentIdentityFromAssociation(record);
			
			if (parentIdentity != null) {
				navigableContent = buildNavigableContent(template, QIdUtil.getParentQId(qId), 
						parentIdentity, navigableContent);
			}
		}
		
		return navigableContent;
	}
	
	private NavigableContent createNavigableContent(Map<String, Object> record, 
			String containerQId, ContentTemplate template, NavigableContent child) {
		
		String label = ContentDataUtil.findPortalLabelValue(record, template, containerQId);
		
		NavigableContent content = new NavigableContent(containerQId, 
				(String) record.get(ContentDataConstants.IDENTITY_KEY), label);
		
		content.setNext(child);
		
		return content;
	}
	

}
