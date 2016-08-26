package com.enablix.app.content.ui.format;

import org.springframework.stereotype.Component;

import com.enablix.core.commons.xsdtopojo.ContentItemClassType;
import com.enablix.core.commons.xsdtopojo.ContentItemType;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.core.ui.TextValue;

@Component
public class NumericValueBuilder implements FieldValueBuilder<TextValue, String>{

	@Override
	public TextValue build(ContentItemType fieldDef, String fieldValue, 
			ContentTemplate template, DisplayContext ctx) {
		return new TextValue((String) fieldValue);
	}

	@Override
	public boolean canHandle(ContentItemType fieldDef) {
		return fieldDef.getType() == ContentItemClassType.NUMERIC;
	}

}
