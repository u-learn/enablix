package com.enablix.app.content.ui.format;

import org.springframework.stereotype.Component;

import com.enablix.core.commons.xsdtopojo.ContentItemClassType;
import com.enablix.core.commons.xsdtopojo.ContentItemType;
import com.enablix.core.ui.TextValue;
import com.enablix.services.util.template.TemplateWrapper;

@Component
public class NumericValueBuilder implements FieldValueBuilder<TextValue, String>{

	@Override
	public TextValue build(ContentItemType fieldDef, String fieldValue, 
			TemplateWrapper template, DisplayContext ctx) {
		return new TextValue((String) fieldValue);
	}

	@Override
	public boolean canHandle(ContentItemType fieldDef) {
		return fieldDef.getType() == ContentItemClassType.NUMERIC;
	}

}
