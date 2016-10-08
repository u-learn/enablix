package com.enablix.app.content.bounded.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.enablix.app.content.bounded.BoundedListBuilder;
import com.enablix.app.content.bounded.DataItem;
import com.enablix.core.commons.xsdtopojo.BoundedType;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.core.commons.xsdtopojo.FixedListDataType;

@Component
public class FixedListBuilder implements BoundedListBuilder {

	@Override
	public Collection<DataItem> buildBoundedList(ContentTemplate template, BoundedType boundedTypeDef) {
		
		Set<DataItem> itemList = new HashSet<>();
		
		for (FixedListDataType item : boundedTypeDef.getFixedList().getData()) {
			itemList.add(new DataItem(item.getId(), item.getLabel()));
		}
		
		return itemList;
	}

	@Override
	public boolean canHandle(BoundedType boundedType) {
		return boundedType.getFixedList() != null;
	}

}
