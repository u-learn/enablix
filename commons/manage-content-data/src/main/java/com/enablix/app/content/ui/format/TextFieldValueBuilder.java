package com.enablix.app.content.ui.format;

import org.springframework.stereotype.Component;

import com.enablix.commons.util.TextLinkifier;
import com.enablix.commons.util.TextLinkifier.LinkDecorator;
import com.enablix.core.commons.xsdtopojo.ContentItemClassType;
import com.enablix.core.commons.xsdtopojo.ContentItemType;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.core.ui.TextValue;

@Component
public class TextFieldValueBuilder implements FieldValueBuilder<TextValue, String> {

	@Override
	public TextValue build(ContentItemType fieldDef, String fieldValue, 
			ContentTemplate template, DisplayContext ctx) {
		
		LinkDecorator linkDecorator = ctx.getLinkDecorator();
		String textVal = linkDecorator == null ? TextLinkifier.linkifyText(String.valueOf(fieldValue))
				: TextLinkifier.linkifyText(String.valueOf(fieldValue), fieldDef.getQualifiedId(), linkDecorator);
		return new TextValue(textVal);
	}

	@Override
	public boolean canHandle(ContentItemType fieldDef) {
		return fieldDef.getType() == ContentItemClassType.TEXT;
	}
	
}
