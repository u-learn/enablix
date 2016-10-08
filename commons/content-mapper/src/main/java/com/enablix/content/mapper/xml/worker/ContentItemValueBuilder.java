package com.enablix.content.mapper.xml.worker;

import com.enablix.core.commons.xsdtopojo.ContentItemMappingType;
import com.enablix.core.commons.xsdtopojo.ContentItemType;

public interface ContentItemValueBuilder<T> {

	T buildValue(ContentItemType contentItem, ContentItemMappingType itemMapping, Object extValue); 
	
	boolean canHandle(ContentItemType contentItem);

}
