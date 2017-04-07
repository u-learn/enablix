package com.enablix.app.content.filter;

import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.ContentItemType;

public interface ContentFilterIdResolver {

	String resolveFilterAttributeId(ContentItemType contentItem, TemplateFacade template);
	
}
