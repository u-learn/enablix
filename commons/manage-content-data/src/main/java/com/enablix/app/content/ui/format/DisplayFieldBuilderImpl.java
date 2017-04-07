package com.enablix.app.content.ui.format;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.commons.util.StringUtil;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.ContentItemClassType;
import com.enablix.core.commons.xsdtopojo.ContentItemType;
import com.enablix.core.ui.DisplayField;
import com.enablix.core.ui.FieldValue;

@Component
public class DisplayFieldBuilderImpl implements DisplayFieldBuilder {

	@Autowired
	private FieldValueBuilderFactory fvBuilderFactory;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public DisplayField<?> build(ContentItemType fieldDef, TemplateFacade template, 
			Map<String, Object> contentRec, DisplayContext ctx) {
		
		FieldValueBuilder builder = fvBuilderFactory.getBuilder(fieldDef);
		
		Object value = getFieldRawValue(fieldDef, contentRec);
		if (value != null && StringUtil.isNotStringAndEmpty(value)) {
			
			FieldValue fieldVal = builder.build(fieldDef, value, template, ctx);
			
			if (fieldVal != null) {
				return new DisplayField(fieldDef.getId(), fieldDef.getLabel(), 
					fieldVal);
			}
		}
		
		return null;
	}

	private Object getFieldRawValue(ContentItemType fieldDef, Map<String, Object> contentRec) {
		
		Object rawValue = contentRec.get(fieldDef.getId());
		
		if (fieldDef.getType() == ContentItemClassType.RICH_TEXT) {
		
			Object richTextValue = contentRec.get(fieldDef.getId() 
									+ ContentDataConstants.RICH_TEXT_FIELD_SUFFIX);
			if (richTextValue != null) {
				rawValue = richTextValue;
			}
		}
		
		return rawValue;
	}

}
