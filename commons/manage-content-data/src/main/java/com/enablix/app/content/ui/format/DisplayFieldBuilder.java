package com.enablix.app.content.ui.format;

import java.util.Map;

import com.enablix.core.commons.xsdtopojo.ContentItemType;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.core.ui.DisplayField;

public interface DisplayFieldBuilder {

	DisplayField<?> build(ContentItemType fieldDef, ContentTemplate template, Map<String, Object> contentRec);
	
}
