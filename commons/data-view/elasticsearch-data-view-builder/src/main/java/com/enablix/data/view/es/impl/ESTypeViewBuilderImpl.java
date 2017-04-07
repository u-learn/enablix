package com.enablix.data.view.es.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.template.service.TemplateManager;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.domain.segment.DataSegment;
import com.enablix.data.view.ContainerDataSegmentFilterBuilder;
import com.enablix.data.view.ContainerVisibilityChecker;
import com.enablix.data.view.DataSegmentAttrFilter;
import com.enablix.data.view.es.ESTypeViewBuilder;

@Component
public class ESTypeViewBuilderImpl implements ESTypeViewBuilder {

	@Autowired
	private TemplateManager templateManager;
	
	@Autowired
	private ContainerVisibilityChecker containerVisibleCheck;
	
	@Autowired
	private ContainerDataSegmentFilterBuilder filterBuilder;
	
	@Override
	public boolean isTypeVisible(String type, DataSegment ds) {

		// assume type is the collection name
		TemplateFacade template = templateManager.getTemplateFacade(ProcessContext.get().getTemplateId());
		ContainerType containerDef = template.getContainerForCollection(type);
		
		return containerVisibleCheck.isContainerVisible(containerDef, ds);
	}

	@Override
	public List<DataSegmentAttrFilter> createDataSegmentFilters(String type, DataSegment dataSegment) {

		// assume type is the collection name
		TemplateFacade template = templateManager.getTemplateFacade(ProcessContext.get().getTemplateId());
		ContainerType containerDef = template.getContainerForCollection(type);
		
		return filterBuilder.createFilters(containerDef, dataSegment);
	}

}
