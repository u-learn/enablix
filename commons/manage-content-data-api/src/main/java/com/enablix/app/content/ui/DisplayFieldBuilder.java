package com.enablix.app.content.ui;

import java.util.Map;

import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.ContentItemType;
import com.enablix.core.ui.DisplayField;

public interface DisplayFieldBuilder {

	DisplayField<?> build(ContentItemType fieldDef, TemplateFacade template, Map<String, Object> contentRec, DisplayContext ctx);
	
}
