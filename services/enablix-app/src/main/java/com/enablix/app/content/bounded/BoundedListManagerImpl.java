package com.enablix.app.content.bounded;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.content.TemplateUtil;
import com.enablix.app.template.service.TemplateManager;
import com.enablix.commons.util.QIdUtil;
import com.enablix.core.commons.xsdtopojo.BoundedType;
import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.commons.xsdtopojo.ContentItemType;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;

@Component
public class BoundedListManagerImpl implements BoundedListManager {

	@Autowired
	private BoundedListBuilderFactory builderFactory;
	
	@Autowired
	private TemplateManager templateMgr;
	
	@Override
	public Collection<DataItem> getBoundedList(String templateId, String contentQId) {
		
		ContentTemplate template = templateMgr.getTemplate(templateId);
		
		ContainerType container = TemplateUtil.findContainer(
				template.getDataDefinition(), QIdUtil.getParentQId(contentQId));
		
		BoundedType boundedType = null;
		
		for (ContentItemType item : container.getContentItem()) {
			if (item.getQualifiedId().equals(contentQId)) {
				boundedType = item.getBounded();
				break;
			}
		}

		Collection<DataItem> items = null;
		if (boundedType != null) {
			items = builderFactory.getBuilder(boundedType).buildBoundedList(template, boundedType);
		}
		
		return items == null ? new ArrayList<DataItem>() : items;
	}

}
