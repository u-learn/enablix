package com.enablix.content.mapper.xml.worker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.core.commons.xsdtopojo.ContentItemMappingType;
import com.enablix.core.commons.xsdtopojo.ContentItemType;

public abstract class BoundedItemValueBuilder implements ContentItemValueBuilder<List<Map<String, String>>> {

	@Override
	public List<Map<String, String>> buildValue(ContentItemType contentItem, ContentItemMappingType itemMapping, Object extValue) {
		
		List<Map<String, String>> itemValue = new ArrayList<>();
		
		if (extValue instanceof String) {
			
			buildAndAddItemValue(itemValue, contentItem, itemMapping, (String) extValue);
			
		} else if (extValue instanceof List<?>) {
			
			for (Object val : (List<?>) extValue) {
				buildAndAddItemValue(itemValue, contentItem, itemMapping, String.valueOf(val));
			}
			
		}
		
		return CollectionUtil.isEmpty(itemValue) ? null : itemValue;
	}
	
	private void buildAndAddItemValue(List<Map<String, String>> itemValue, 
			ContentItemType contentItem, ContentItemMappingType itemMapping, String extValue) {
		
		Map<String, String> boundedValue = getBoundedValue(contentItem, itemMapping, (String) extValue);
		if (boundedValue != null && !boundedValue.isEmpty()) {
			itemValue.add(boundedValue);
		}
	}
	
	protected Map<String, String> createBoundedValue(String id, String label) {
		
		Map<String, String> value = new HashMap<>();
		
		value.put(ContentDataConstants.BOUNDED_ID_ATTR, id);
		value.put(ContentDataConstants.BOUNDED_LABEL_ATTR, label);
		
		return value;
	}
	
	protected abstract Map<String, String> getBoundedValue(
			ContentItemType contentItem, ContentItemMappingType itemMapping, String extValue);

}
