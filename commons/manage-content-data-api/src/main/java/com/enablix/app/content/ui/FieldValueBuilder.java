package com.enablix.app.content.ui;

import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.ContentItemType;
import com.enablix.core.ui.FieldValue;

public interface FieldValueBuilder<T extends FieldValue, I> {

	T build(ContentItemType fieldDef, I fieldValue, TemplateFacade template, DisplayContext ctx);
	
	boolean isEmptyValue(ContentItemType fieldDef, I fieldValue, TemplateFacade template);
	
	boolean canHandle(ContentItemType fieldDef);
	
}
