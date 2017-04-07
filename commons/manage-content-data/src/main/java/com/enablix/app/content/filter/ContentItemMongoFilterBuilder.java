package com.enablix.app.content.filter;

import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.ContentItemType;
import com.enablix.core.mongo.search.SearchFilter;

public interface ContentItemMongoFilterBuilder {

	SearchFilter createFilter(ContentItemType contentItem, Object value, TemplateFacade template);
	
}
