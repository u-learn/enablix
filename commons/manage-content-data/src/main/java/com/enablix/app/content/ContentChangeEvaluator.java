package com.enablix.app.content;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.commons.xsdtopojo.ContentItemType;
import com.enablix.core.domain.content.ContentChangeDelta;
import com.enablix.core.domain.content.ContentChangeDelta.AttributeChange;

@Component
public class ContentChangeEvaluator {

	public ContentChangeDelta findDelta(Map<String, Object> oldRecord, Map<String, Object> newRecord, ContainerType container) {
		
		ContentChangeDelta delta = new ContentChangeDelta();
		
		for (ContentItemType contentItem : container.getContentItem()) {
			
			String attrId = contentItem.getId();
			
			Object oldValue = getAttributeValue(attrId, oldRecord);
			Object newValue = getAttributeValue(attrId, newRecord);
			
			if (oldValue != null && newValue != null) {
				
				if (!oldValue.equals(newValue)) {
					delta.addChangedAttribute(new AttributeChange(attrId, newValue, oldValue));
				}
				
			} else if (oldValue != newValue) {
				delta.addChangedAttribute(new AttributeChange(attrId, newValue, oldValue));
			}
			
		}
		
		return delta;
		
	}
	
	private Object getAttributeValue(String attrId, Map<String, Object> record) {
		if (record != null) {
			return record.get(attrId);
		}
		return null;
	}
}
