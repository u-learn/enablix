package com.enablix.content.mapper.xml.worker;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.enablix.core.commons.xsdtopojo.BoundedFixedListType;
import com.enablix.core.commons.xsdtopojo.ContentItemClassType;
import com.enablix.core.commons.xsdtopojo.ContentItemListAttrType;
import com.enablix.core.commons.xsdtopojo.ContentItemMappingType;
import com.enablix.core.commons.xsdtopojo.ContentItemType;
import com.enablix.core.commons.xsdtopojo.FixedListDataType;

@Component
public class FixedListBoundedValueBuilder extends BoundedItemValueBuilder {

	@Override
	public boolean canHandle(ContentItemType contentItem) {
		return contentItem.getType() == ContentItemClassType.BOUNDED
				&& contentItem.getBounded().getFixedList() != null;
	}

	@Override
	protected Map<String, String> getBoundedValue(ContentItemType contentItem, ContentItemMappingType itemMapping,
			String extValue) {
		
		BoundedFixedListType fixedList = contentItem.getBounded().getFixedList();
		
		if (fixedList != null) {
			
			ContentItemListAttrType listAttribute = itemMapping.getListAttribute();
			
			boolean matchLabel = listAttribute == null || listAttribute == ContentItemListAttrType.LABEL;
			
			for (FixedListDataType listData : fixedList.getData()) {
			
				if ((matchLabel && listData.getLabel().equals(extValue))
						|| (!matchLabel && listData.getId().equals(extValue))) {
					
					return createBoundedValue(listData.getId(), listData.getLabel());
				}
			}
		}
		
		return null;
	}

}
