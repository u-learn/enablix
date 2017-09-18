package com.enablix.app.content.ui.format;

import java.util.Collection;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.enablix.app.content.ui.DisplayContext;
import com.enablix.app.content.ui.FieldValueBuilder;
import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.BoundedFixedListType;
import com.enablix.core.commons.xsdtopojo.BoundedRefListType;
import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.commons.xsdtopojo.ContentItemClassType;
import com.enablix.core.commons.xsdtopojo.ContentItemType;
import com.enablix.core.ui.ContentRef;
import com.enablix.core.ui.ListValue;
import com.enablix.core.ui.TextValue;

@Component
public class ContentRefListValueBuilder implements FieldValueBuilder<ListValue, Collection<Map<String, Object>>> {

	@Override
	public ListValue build(ContentItemType fieldDef, Collection<Map<String, Object>> fieldValue, 
			TemplateFacade template, DisplayContext ctx) {
		
		ListValue listVal = new ListValue();
		
		BoundedFixedListType fixedList = fieldDef.getBounded().getFixedList();
		
		if (fixedList != null) {
			
			addTextValues(fieldValue, listVal);
			
		} else {
			
			BoundedRefListType refList = fieldDef.getBounded().getRefList();
			String containerId = refList.getDatastore().getStoreId();
			
			ContainerType containerDef = template.getContainerDefinition(containerId);
			
			if (containerDef == null || containerDef.isRefData()) {
				
				addTextValues(fieldValue, listVal);
				
			} else {
				
				for (Map<String, Object> val : fieldValue) {
				
					ContentRef contentRef = new ContentRef();
					contentRef.setContainerQId(containerId);
					contentRef.setTextValue((String) val.get(ContentDataConstants.BOUNDED_LABEL_ATTR));
					contentRef.setContentIdentity((String) val.get(ContentDataConstants.ID_FLD_KEY));
					
					listVal.addValue(contentRef);
				}
			}
		}
		
		return listVal.isEmpty() ? null : listVal;
	}

	private void addTextValues(Collection<Map<String, Object>> fieldValue, ListValue listVal) {
		for (Map<String, Object> val : fieldValue) {
			listVal.addValue(new TextValue((String) val.get(ContentDataConstants.BOUNDED_LABEL_ATTR)));
		}
	}

	@Override
	public boolean canHandle(ContentItemType fieldDef) {
		return fieldDef.getType() == ContentItemClassType.BOUNDED;
	}

	@Override
	public boolean isEmptyValue(ContentItemType fieldDef, Collection<Map<String, Object>> fieldValue,
			TemplateFacade template) {
		return CollectionUtil.isEmpty(fieldValue);
	}

}
