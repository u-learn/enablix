package com.enablix.content.mapper.xml.worker;

import java.util.List;

import org.springframework.stereotype.Component;

import com.enablix.core.commons.xsdtopojo.ContentItemClassType;
import com.enablix.core.commons.xsdtopojo.ContentItemMappingType;
import com.enablix.core.commons.xsdtopojo.ContentItemType;

@Component
public class TextItemValueBuilder implements ContentItemValueBuilder<String> {

	@Override
	public String buildValue(ContentItemType contentItem, ContentItemMappingType itemMapping, Object extValue) {
		
		if (extValue instanceof List<?>) {
			List<?> listValue = (List<?>) extValue;
			extValue = listValue.get(0);
		}
		
		return String.valueOf(extValue);
	}

	@Override
	public boolean canHandle(ContentItemType contentItem) {
		return contentItem.getType() == ContentItemClassType.TEXT 
				|| contentItem.getType() == ContentItemClassType.RICH_TEXT;
	}

	
	
}
