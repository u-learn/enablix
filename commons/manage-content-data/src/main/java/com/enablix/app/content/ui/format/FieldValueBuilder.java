package com.enablix.app.content.ui.format;

import com.enablix.core.commons.xsdtopojo.ContentItemType;
import com.enablix.core.ui.FieldValue;
import com.enablix.services.util.template.TemplateWrapper;

public interface FieldValueBuilder<T extends FieldValue, I> {

	T build(ContentItemType fieldDef, I fieldValue, TemplateWrapper template, DisplayContext ctx);
	
	boolean canHandle(ContentItemType fieldDef);
	
}
