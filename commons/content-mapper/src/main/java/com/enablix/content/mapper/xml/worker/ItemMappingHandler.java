package com.enablix.content.mapper.xml.worker;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.commons.content.ContentParser;
import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.content.mapper.EnablixContent;
import com.enablix.content.mapper.ExternalContent;
import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.commons.xsdtopojo.ContentItemMappingType;
import com.enablix.core.commons.xsdtopojo.ContentItemType;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.services.util.TemplateUtil;

@Component
public class ItemMappingHandler {

	@Autowired
	private ContentItemValueBuilderFactory builderFactory;
	
	public void setEnablixItemValue(ContentItemMappingType itemMapping, 
			ExternalContent extContent, EnablixContent ebxContent, ContentTemplate template) {
	
		ContainerType containerDef = TemplateUtil.findContainer(
				template.getDataDefinition(), extContent.getContentQId());
		
		for (ContentItemType contentItem : containerDef.getContentItem()) {
		
			if (itemMapping.getItemId().equals(contentItem.getId())) {
			
				List<Object> extItemValue = ContentParser.getValue(extContent.getData(), itemMapping.getValue());
				
				if (CollectionUtil.isNotEmpty(extItemValue)) {
					
					ContentItemValueBuilder<?> builder = builderFactory.getBuilder(contentItem);
					Object ebxItemValue = builder.buildValue(contentItem, itemMapping, extItemValue);
					
					ebxContent.getData().put(contentItem.getId(), ebxItemValue);
				}
			}
		}
	}
	
}
