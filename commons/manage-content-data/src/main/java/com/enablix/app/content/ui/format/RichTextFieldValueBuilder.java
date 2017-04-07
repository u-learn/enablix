package com.enablix.app.content.ui.format;

import org.springframework.stereotype.Component;

import com.enablix.commons.util.StringUtil;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.ContentItemClassType;
import com.enablix.core.commons.xsdtopojo.ContentItemType;
import com.enablix.core.ui.TextValue;

@Component
public class RichTextFieldValueBuilder implements FieldValueBuilder<TextValue, String> {

	@Override
	public TextValue build(ContentItemType fieldDef, String fieldValue, 
			TemplateFacade template, DisplayContext ctx) {
		return StringUtil.hasText(fieldValue) ? new TextValue(fieldValue) : null;
	}

	@Override
	public boolean canHandle(ContentItemType fieldDef) {
		return fieldDef.getType() == ContentItemClassType.RICH_TEXT;
	}
	
}
