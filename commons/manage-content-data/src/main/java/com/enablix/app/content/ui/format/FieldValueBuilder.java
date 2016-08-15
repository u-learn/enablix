package com.enablix.app.content.ui.format;

import com.enablix.core.commons.xsdtopojo.ContentItemType;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.core.ui.FieldValue;

public interface FieldValueBuilder<T extends FieldValue, I> {

	T build(ContentItemType fieldDef, I fieldValue, ContentTemplate template);
	
	boolean canHandle(ContentItemType fieldDef);
	
}
