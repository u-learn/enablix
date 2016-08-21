package com.enablix.app.content.ui.format;

import org.springframework.stereotype.Component;

import com.enablix.commons.util.StringUtil;
import com.enablix.core.commons.xsdtopojo.ContentItemClassType;
import com.enablix.core.commons.xsdtopojo.ContentItemType;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.core.ui.TextValue;

@Component
public class TextFieldValueBuilder implements FieldValueBuilder<TextValue, String> {

	@Override
	public TextValue build(ContentItemType fieldDef, String fieldValue, ContentTemplate template) {
		return new TextValue(StringUtil.linkifyText(String.valueOf(fieldValue)));
	}

	@Override
	public boolean canHandle(ContentItemType fieldDef) {
		return fieldDef.getType() == ContentItemClassType.TEXT;
	}

}
