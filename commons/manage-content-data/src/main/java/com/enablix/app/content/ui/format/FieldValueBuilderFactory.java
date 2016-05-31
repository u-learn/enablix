package com.enablix.app.content.ui.format;

import com.enablix.core.commons.xsdtopojo.ContentItemType;

public interface FieldValueBuilderFactory {

	FieldValueBuilder<?, ?> getBuilder(ContentItemType fieldDef);
	
}
