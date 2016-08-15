package com.enablix.app.content.ui.format;

import java.util.Collection;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.core.commons.xsdtopojo.BoundedFixedListType;
import com.enablix.core.commons.xsdtopojo.BoundedRefListType;
import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.commons.xsdtopojo.ContentItemClassType;
import com.enablix.core.commons.xsdtopojo.ContentItemType;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.core.ui.ContentRef;
import com.enablix.core.ui.ListValue;
import com.enablix.core.ui.TextValue;
import com.enablix.services.util.TemplateUtil;

@Component
public class ContentRefListValueBuilder implements FieldValueBuilder<ListValue, Collection<Map<String, Object>>> {

	@Override
	public ListValue build(ContentItemType fieldDef, Collection<Map<String, Object>> fieldValue, ContentTemplate template) {
		
		ListValue listVal = new ListValue();
		
		BoundedFixedListType fixedList = fieldDef.getBounded().getFixedList();
		
		if (fixedList != null) {
			
			addTextValues(fieldValue, listVal);
			
		} else {
			
			BoundedRefListType refList = fieldDef.getBounded().getRefList();
			String containerId = refList.getDatastore().getStoreId();
			
			ContainerType containerDef = TemplateUtil.findContainer(template.getDataDefinition(), containerId);
			
			if (containerDef == null) {
				
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
		
		return listVal;
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

}
