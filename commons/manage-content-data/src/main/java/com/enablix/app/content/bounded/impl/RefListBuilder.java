package com.enablix.app.content.bounded.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.content.bounded.BoundedListBuilder;
import com.enablix.app.content.bounded.DataItem;
import com.enablix.commons.util.StringUtil;
import com.enablix.core.commons.xsdtopojo.BoundedListDatastoreType;
import com.enablix.core.commons.xsdtopojo.BoundedRefListType;
import com.enablix.core.commons.xsdtopojo.BoundedType;
import com.enablix.core.mongo.content.ContentCrudService;
import com.enablix.services.util.template.TemplateWrapper;

@Component
public class RefListBuilder implements BoundedListBuilder {

	@Autowired
	private ContentCrudService contentCrudService;
	
	@Override
	public Collection<DataItem> buildBoundedList(TemplateWrapper template, BoundedType boundedTypeDef) {
		
		Set<DataItem> itemList = new HashSet<>();
		
		BoundedRefListType refListType = boundedTypeDef.getRefList();
		BoundedListDatastoreType boundedListDS = refListType.getDatastore();
		
		String collectionName = template.getCollectionName(boundedListDS.getStoreId());
		
		List<Map<String, Object>> records = contentCrudService.findAllRecord(collectionName);
		
		if (records != null) {
		
			for (Map<String, Object> record : records) {
			
				String dataId = getStringValue(record.get(boundedListDS.getDataId()));
				String dataLabel = getStringValue(record.get(boundedListDS.getDataLabel()));
				
				if (!StringUtil.isEmpty(dataId) && !StringUtil.isEmpty(dataLabel)) {
					itemList.add(new DataItem(dataId, dataLabel));
				}
			}
		}
		
		return itemList;
	}

	private String getStringValue(Object data) {
		return data == null ? null : String.valueOf(data);
	}
	
	@Override
	public boolean canHandle(BoundedType boundedType) {
		return boundedType.getRefList() != null;
	}

}
