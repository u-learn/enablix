package com.enablix.app.content.ui.format;

import java.util.Collection;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.enablix.app.content.ui.DisplayContext;
import com.enablix.app.content.ui.FieldValueBuilder;
import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.ContentItemClassType;
import com.enablix.core.commons.xsdtopojo.ContentItemType;
import com.enablix.core.ui.ListValue;

@Component
public class ContentStackValueBuilder implements FieldValueBuilder<ListValue, Collection<Map<String, Object>>> {

	@Override
	public ListValue build(ContentItemType fieldDef, Collection<Map<String, Object>> fieldValue, 
			TemplateFacade template, DisplayContext ctx) {
		
		ListValue listVal = new ListValue();
		
		return listVal.isEmpty() ? null : listVal;
	}

	@Override
	public boolean canHandle(ContentItemType fieldDef) {
		return fieldDef.getType() == ContentItemClassType.CONTENT_STACK;
	}

	@Override
	public boolean isEmptyValue(ContentItemType fieldDef, Collection<Map<String, Object>> fieldValue,
			TemplateFacade template) {
		return CollectionUtil.isEmpty(fieldValue);
	}

}
