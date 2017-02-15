package com.enablix.app.content.ui.format;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.core.commons.xsdtopojo.ContentItemType;
import com.enablix.core.ui.DisplayField;
import com.enablix.core.ui.FieldValue;
import com.enablix.services.util.template.TemplateWrapper;

@Component
public class DisplayFieldBuilderImpl implements DisplayFieldBuilder {

	@Autowired
	private FieldValueBuilderFactory fvBuilderFactory;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public DisplayField<?> build(ContentItemType fieldDef, TemplateWrapper template, 
			Map<String, Object> contentRec, DisplayContext ctx) {
		
		FieldValueBuilder builder = fvBuilderFactory.getBuilder(fieldDef);
		
		Object value = contentRec.get(fieldDef.getId());
		if (value != null) {
			
			FieldValue fieldVal = builder.build(fieldDef, value, template, ctx);
			
			if (fieldVal != null) {
				return new DisplayField(fieldDef.getId(), fieldDef.getLabel(), 
					fieldVal);
			}
		}
		
		return null;
	}

}
