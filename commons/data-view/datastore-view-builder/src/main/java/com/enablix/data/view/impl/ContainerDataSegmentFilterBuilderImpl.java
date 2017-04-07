package com.enablix.data.view.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.content.filter.ContentFilterIdResolver;
import com.enablix.app.template.service.TemplateManager;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.commons.xsdtopojo.ContentItemType;
import com.enablix.core.commons.xsdtopojo.DataSegmentDefinitionType;
import com.enablix.core.domain.segment.DataSegment;
import com.enablix.core.domain.segment.DataSegmentAttribute;
import com.enablix.data.view.ContainerDataSegmentFilterBuilder;
import com.enablix.data.view.DataSegmentAttrFilter;
import com.enablix.services.util.TemplateUtil;

@Component
public class ContainerDataSegmentFilterBuilderImpl implements ContainerDataSegmentFilterBuilder {

	@Autowired
	private ContentFilterIdResolver filterIdResolver;
	
	@Autowired
	private TemplateManager templateManager;
	
	@Override
	public List<DataSegmentAttrFilter> createFilters(String containerQId, DataSegment dataSegment) {
		
		TemplateFacade template = templateManager.getTemplateFacade(ProcessContext.get().getTemplateId());
		ContainerType containerDef = template.getContainerDefinition(containerQId);
		
		return buildFilters(containerDef, dataSegment, template);
	}
	
	protected List<DataSegmentAttrFilter> buildFilters(ContainerType containerDef, DataSegment dataSegment, TemplateFacade template) {
		
		// 1. Iterate through the data segment attributes defined in the template
		// 2. Look for the corresponding data segment attribute value in data segment
		// 3. if found, resolve the filter id for each data segment attribute

		List<DataSegmentAttrFilter> filters = new ArrayList<>();
		
		DataSegmentDefinitionType dataSegmentDef = template.getTemplate().getDataSegmentDefinition();
		
		if (containerDef != null && dataSegmentDef != null) {
			
			for (ContentItemType attr : dataSegmentDef.getSegmentAttr()) {
				
				ContentItemType contentItem = TemplateUtil.findMatchingContentItem(containerDef, attr);
				
				if (contentItem != null) {
					
					DataSegmentAttribute dataSegmentAttr = dataSegment.findAttribute(attr.getId());
					
					if (dataSegmentAttr != null) {
						String recordFilterId = filterIdResolver.resolveFilterAttributeId(contentItem, template);
						filters.add(new DataSegmentAttrFilter(dataSegmentAttr, recordFilterId));
					}
				}
			}
		}
		
		return filters;
	}

	@Override
	public List<DataSegmentAttrFilter> createFilters(ContainerType containerDef, DataSegment dataSegment) {
		TemplateFacade template = templateManager.getTemplateFacade(ProcessContext.get().getTemplateId());
		return buildFilters(containerDef, dataSegment, template);
	}

}
