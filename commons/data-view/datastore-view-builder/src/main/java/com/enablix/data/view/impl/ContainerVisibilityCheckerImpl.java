package com.enablix.data.view.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.template.service.TemplateManager;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.commons.xsdtopojo.ContentItemType;
import com.enablix.core.domain.segment.DataSegment;
import com.enablix.core.domain.segment.DataSegmentAttribute;
import com.enablix.core.domain.segment.DataSegmentAttribute.Presence;
import com.enablix.data.view.ContainerVisibilityChecker;
import com.enablix.services.util.TemplateUtil;

@Component
public class ContainerVisibilityCheckerImpl implements ContainerVisibilityChecker {

	@Autowired
	private TemplateManager templateManager;
	
	@Override
	public boolean isContainerVisible(String containerQId, DataSegment dataSegment) {

		TemplateFacade template = templateManager.getTemplateFacade(ProcessContext.get().getTemplateId());
		ContainerType containerDef = template.getContainerDefinition(containerQId);
		
		return checkContainerVisibility(containerDef, dataSegment, template);
	}

	@Override
	public boolean isContainerVisible(ContainerType containerDef, DataSegment ds) {
		TemplateFacade template = templateManager.getTemplateFacade(ProcessContext.get().getTemplateId());
		return checkContainerVisibility(containerDef, ds, template);
	}
	
	protected boolean checkContainerVisibility(ContainerType containerDef, DataSegment ds, TemplateFacade template) {
		
		boolean visible = false;
		
		if (containerDef != null) {
			
			for (DataSegmentAttribute dsAttr : ds.getAttributes()) {
			
				if (dsAttr.getPresence() == Presence.OPTIONAL) {
					visible = true;
					continue;
				}
				
				// presence is REQUIRED, then lets try to find the matching content item
				ContentItemType dsAttrDefinition = template.getDataSegmentAttrDefinition(dsAttr.getId());
				
				if (dsAttrDefinition != null) {
					
					ContentItemType matchedContentItem = TemplateUtil.findMatchingContentItem(containerDef, dsAttrDefinition);
					
					visible = matchedContentItem != null;
							
				} else {
					// data segment has an attribute but the data segment definition in the template does
					// not have the attribute, so we can ignore this attribute in data segment
					visible = true;
				}
				
				if (!visible) {
					// if based on one of the attr, the collection is not visible, then that is final
					break;
				}
			}
			
		}
		
		return visible;
	}

}
