package com.enablix.content.mapper.xml.worker;

import com.enablix.core.commons.xsdtopojo.ContentItemType;

public interface ContentItemValueBuilderFactory {

	ContentItemValueBuilder<?> getBuilder(ContentItemType contentItem);
	
}
