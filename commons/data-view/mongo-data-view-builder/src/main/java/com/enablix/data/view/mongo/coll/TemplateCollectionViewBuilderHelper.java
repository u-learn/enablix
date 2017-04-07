package com.enablix.data.view.mongo.coll;

import java.util.List;
import java.util.Map;

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
import com.enablix.data.view.mongo.AttributeMatcher;

@Component
public class TemplateCollectionViewBuilderHelper implements CollectionViewBuilderHelper {

	@Autowired
	private TemplateManager templateManager;
	
	@Autowired
	private ContainerAttributeMatcher attributeMatcher;
	
	@Autowired
	private ContainerVisibilityChecker containerVisibilityCheck;
	
	@Autowired
	private ContainerDataSegmentFilterBuilder containerFilterBuilder;
	
	@Override
	public boolean isCollectionVisible(String collectionName, DataSegment ds) {
		
		// if this collection does not have all the REQUIRED data segment attributes as content items, 
		// then this collection is not visible at all
		
		boolean visible = false;
		TemplateFacade template = templateManager.getTemplateFacade(ProcessContext.get().getTemplateId());
		
		ContainerType containerDef = template.getContainerForCollection(collectionName);
		
		if (containerDef != null) {
			visible = containerVisibilityCheck.isContainerVisible(containerDef, ds);
		}
		
		return visible;
	}

	@Override
	public boolean canHandle(String collectionName) {
		TemplateFacade template = templateManager.getTemplateFacade(ProcessContext.get().getTemplateId());
		return template.getContainerForCollection(collectionName) != null;
	}

	@Override
	public List<DataSegmentAttrFilter> createDataSegmentFilters(String collectionName, DataSegment dataSegment) {
		
		TemplateFacade template = templateManager.getTemplateFacade(ProcessContext.get().getTemplateId());
		ContainerType containerDef = template.getContainerForCollection(collectionName);
		
		return containerFilterBuilder.createFilters(containerDef, dataSegment);
	}

	@Override
	public AttributeMatcher<Map<String, Object>> attributeMatcher() {
		return attributeMatcher;
	}

}
