package com.enablix.app.content.bounded;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.template.service.TemplateManager;
import com.enablix.commons.util.QIdUtil;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.commons.xsdtopojo.BoundedType;
import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.commons.xsdtopojo.ContentItemType;
import com.enablix.services.util.template.TemplateWrapper;

@Component
public class BoundedListManagerImpl implements BoundedListManager {

	@Autowired
	private BoundedListBuilderFactory builderFactory;
	
	@Autowired
	private TemplateManager templateMgr;
	
	@Override
	public Collection<DataItem> getBoundedList(String templateId, String contentQId) {
		
		TemplateWrapper template = templateMgr.getTemplateWrapper(templateId);
		
		ContainerType container = template.getContainerDefinition(QIdUtil.getParentQId(contentQId));
		
		BoundedType boundedType = null;
		
		for (ContentItemType item : container.getContentItem()) {
			if (item.getQualifiedId().equals(contentQId)) {
				boundedType = item.getBounded();
				break;
			}
		}

		return fetchBoundedListDataItems(template, boundedType);
	}

	private Collection<DataItem> fetchBoundedListDataItems(TemplateWrapper template, BoundedType boundedType) {
		
		Collection<DataItem> items = null;
		
		if (boundedType != null) {
			items = builderFactory.getBuilder(boundedType).buildBoundedList(template, boundedType);
		}
		
		return items == null ? new ArrayList<DataItem>() : items;
	}
	
	@Override
	public Collection<DataItem> getBoundedList(BoundedType boundedType) {
		TemplateWrapper template = templateMgr.getTemplateWrapper(ProcessContext.get().getTemplateId());
		return fetchBoundedListDataItems(template, boundedType);
	}

}
