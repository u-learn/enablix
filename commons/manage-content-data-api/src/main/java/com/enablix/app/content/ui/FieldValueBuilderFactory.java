package com.enablix.app.content.ui;

import com.enablix.core.commons.xsdtopojo.ContentItemType;

public interface FieldValueBuilderFactory {

	FieldValueBuilder<?, ?> getBuilder(ContentItemType fieldDef);
	
}
