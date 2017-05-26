package com.enablix.content.mapper.xml.worker;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.commons.content.ContentParser;
import com.enablix.commons.util.StringUtil;
import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.content.mapper.EnablixContent;
import com.enablix.content.mapper.ExternalContent;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.commons.xsdtopojo.ContentItemMappingType;
import com.enablix.core.commons.xsdtopojo.ContentItemType;

@Component
public class ItemMappingHandler {

	@Autowired
	private ContentItemValueBuilderFactory builderFactory;
	
	public void setEnablixItemValue(ContentItemMappingType itemMapping, 
			ExternalContent extContent, EnablixContent ebxContent, TemplateFacade template) {
	
		ContainerType containerDef = template.getContainerDefinition(extContent.getContentQId());
		
		for (ContentItemType contentItem : containerDef.getContentItem()) {
		
			if (itemMapping.getItemId().equals(contentItem.getId())) {
			
				List<Object> extItemValue = null;
				
				String extValueExpr = itemMapping.getValue();
				
				if (!StringUtil.hasText(extValueExpr)) {
				
					extItemValue = getItemDefaultValue(itemMapping);
					
				} else {
					
					extItemValue = ContentParser.getValue(extContent.getData(), extValueExpr);
					
					if (CollectionUtil.isEmpty(extItemValue)) {
						extItemValue = getItemDefaultValue(itemMapping);
					}
				}
				
				if (CollectionUtil.isNotEmpty(extItemValue)) {
					
					ContentItemValueBuilder<?> builder = builderFactory.getBuilder(contentItem);
					Object ebxItemValue = builder.buildValue(contentItem, itemMapping, extItemValue);
					
					ebxContent.getData().put(contentItem.getId(), ebxItemValue);
				}
			}
		}
	}

	private List<Object> getItemDefaultValue(ContentItemMappingType itemMapping) {
		
		List<Object> extItemValue = null;
		
		String defaultValue = itemMapping.getDefault();
		
		if (StringUtil.hasText(defaultValue)) {
			extItemValue = new ArrayList<>();
			extItemValue.add(defaultValue);
		}
		
		return extItemValue;
	}
	
}
