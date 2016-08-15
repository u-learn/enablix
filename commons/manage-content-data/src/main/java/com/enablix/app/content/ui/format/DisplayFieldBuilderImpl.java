package com.enablix.app.content.ui.format;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.core.commons.xsdtopojo.ContentItemType;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.core.ui.DisplayField;

@Component
public class DisplayFieldBuilderImpl implements DisplayFieldBuilder {

	@Autowired
	private FieldValueBuilderFactory fvBuilderFactory;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public DisplayField<?> build(ContentItemType fieldDef, ContentTemplate template, Map<String, Object> contentRec) {
		
		FieldValueBuilder builder = fvBuilderFactory.getBuilder(fieldDef);
		
		Object value = contentRec.get(fieldDef.getId());
		if (value != null) {
			return new DisplayField(fieldDef.getId(), fieldDef.getLabel(), 
					builder.build(fieldDef, value, template));
		}
		
		return null;
	}

}
