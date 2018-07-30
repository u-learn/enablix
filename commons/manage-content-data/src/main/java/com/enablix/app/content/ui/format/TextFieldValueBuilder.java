package com.enablix.app.content.ui.format;

import org.springframework.stereotype.Component;

import com.enablix.app.content.ui.DisplayContext;
import com.enablix.app.content.ui.FieldValueBuilder;
import com.enablix.commons.util.StringUtil;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.ContentItemClassType;
import com.enablix.core.commons.xsdtopojo.ContentItemType;
import com.enablix.core.ui.TextValue;

@Component
public class TextFieldValueBuilder implements FieldValueBuilder<TextValue, String> {

	@Override
	public TextValue build(ContentItemType fieldDef, String fieldValue, 
			TemplateFacade template, DisplayContext ctx) {
		return StringUtil.hasText(fieldValue) ? new TextValue(fieldValue) : null;
	}

	@Override
	public boolean canHandle(ContentItemType fieldDef) {
		return fieldDef.getType() == ContentItemClassType.TEXT
				|| fieldDef.getType() == ContentItemClassType.URL;
	}

	@Override
	public boolean isEmptyValue(ContentItemType fieldDef, String fieldValue, TemplateFacade template) {
		return StringUtil.isEmpty(fieldValue);
	}
	
}
