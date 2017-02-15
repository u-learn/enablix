package com.enablix.app.content.ui.format;

import java.util.Map;

import com.enablix.core.commons.xsdtopojo.ContentItemType;
import com.enablix.core.ui.DisplayField;
import com.enablix.services.util.template.TemplateWrapper;

public interface DisplayFieldBuilder {

	DisplayField<?> build(ContentItemType fieldDef, TemplateWrapper template, Map<String, Object> contentRec, DisplayContext ctx);
	
}
