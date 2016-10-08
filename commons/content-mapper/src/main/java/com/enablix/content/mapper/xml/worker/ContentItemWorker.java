package com.enablix.content.mapper.xml.worker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.content.mapper.EnablixContent;
import com.enablix.content.mapper.ExternalContent;
import com.enablix.content.mapper.xml.MappingWorker;
import com.enablix.core.commons.xsdtopojo.ContentContainerMappingType;
import com.enablix.core.commons.xsdtopojo.ContentItemMappingType;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;

@Component
public class ContentItemWorker implements MappingWorker {

	@Autowired
	private ItemMappingHandler itemMappingHandler;
	
	@Override
	public float executionOrder() {
		return MappingWorkerExecOrder.CONTENT_ITEM_WORKER;
	}

	@Override
	public void execute(ContentContainerMappingType containerMapping, ExternalContent extContent,
			EnablixContent ebxContent, ContentTemplate template) {
		
		for (ContentItemMappingType itemMapping : containerMapping.getContentItemMappings().getItemMapping()) {
			itemMappingHandler.setEnablixItemValue(itemMapping, extContent, ebxContent, template);
		}
		
	}

}
