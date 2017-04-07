package com.enablix.app.content.bounded;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.template.service.TemplateManager;
import com.enablix.commons.util.QIdUtil;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.BoundedType;
import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.commons.xsdtopojo.ContentItemType;
import com.enablix.data.view.DataView;

@Component
public class BoundedListManagerImpl implements BoundedListManager {

	@Autowired
	private BoundedListBuilderFactory builderFactory;
	
	@Autowired
	private TemplateManager templateMgr;
	
	@Override
	public Collection<DataItem> getBoundedList(String templateId, String contentQId, DataView view) {
		
		TemplateFacade template = templateMgr.getTemplateFacade(templateId);
		
		ContainerType container = template.getContainerDefinition(QIdUtil.getParentQId(contentQId));
		
		BoundedType boundedType = null;
		
		for (ContentItemType item : container.getContentItem()) {
			if (item.getQualifiedId().equals(contentQId)) {
				boundedType = item.getBounded();
				break;
			}
		}

		return fetchBoundedListDataItems(template, boundedType, view);
	}

	private Collection<DataItem> fetchBoundedListDataItems(
			TemplateFacade template, BoundedType boundedType, DataView view) {
		
		Collection<DataItem> items = null;
		
		if (boundedType != null) {
			items = builderFactory.getBuilder(boundedType).buildBoundedList(template, boundedType, view);
		}
		
		return items == null ? new ArrayList<DataItem>() : items;
	}
	
	@Override
	public Collection<DataItem> getBoundedList(BoundedType boundedType, DataView view) {
		TemplateFacade template = templateMgr.getTemplateFacade(ProcessContext.get().getTemplateId());
		return fetchBoundedListDataItems(template, boundedType, view);
	}

}
