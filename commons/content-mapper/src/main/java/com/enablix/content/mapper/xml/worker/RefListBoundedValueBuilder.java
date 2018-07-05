package com.enablix.content.mapper.xml.worker;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import com.enablix.commons.util.StringUtil;
import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.api.ConditionOperator;
import com.enablix.core.commons.xsdtopojo.BoundedListDatastoreType;
import com.enablix.core.commons.xsdtopojo.BoundedRefListType;
import com.enablix.core.commons.xsdtopojo.ContentItemClassType;
import com.enablix.core.commons.xsdtopojo.ContentItemListAttrType;
import com.enablix.core.commons.xsdtopojo.ContentItemMappingType;
import com.enablix.core.commons.xsdtopojo.ContentItemType;
import com.enablix.core.mongo.content.ContentCrudService;
import com.enablix.core.mongo.search.StringFilter;
import com.enablix.core.mongo.view.MongoDataView;
import com.enablix.services.util.DatastoreUtil;

@Component
public class RefListBoundedValueBuilder extends BoundedItemValueBuilder {

	@Autowired
	private ContentCrudService contentCrudService;
	
	@Override
	public boolean canHandle(ContentItemType contentItem) {
		return contentItem.getType() == ContentItemClassType.BOUNDED
				&& contentItem.getBounded().getRefList() != null;
	}

	@Override
	protected Map<String, String> getBoundedValue(ContentItemType contentItem, ContentItemMappingType itemMapping,
			String extValue) {
		
		String templateId = ProcessContext.get().getTemplateId();
		
		BoundedRefListType refListType = contentItem.getBounded().getRefList();
		BoundedListDatastoreType boundedListDS = refListType.getDatastore();
		
		String collectionName = DatastoreUtil.getCollectionName(templateId, boundedListDS.getStoreId());
		
		ContentItemListAttrType listAttr = itemMapping.getListAttribute();
		String propName = listAttr == null || listAttr == ContentItemListAttrType.LABEL ?
				boundedListDS.getDataLabel() : boundedListDS.getDataId();
		
		StringFilter refDataFilter = new StringFilter(propName, extValue, ConditionOperator.EQ);
		List<Map<String, Object>> matchedRecords = contentCrudService.findAllRecordForCriteria(
				collectionName, refDataFilter.toPredicate(new Criteria()), MongoDataView.ALL_DATA);
		
		if (CollectionUtil.isNotEmpty(matchedRecords)) {
			
			Map<String, Object> record = matchedRecords.get(0);
			
			String dataId = StringUtil.stringValue(record.get(boundedListDS.getDataId()));
			String dataLabel = StringUtil.stringValue(record.get(boundedListDS.getDataLabel()));
			
			if (!StringUtil.isEmpty(dataId) && !StringUtil.isEmpty(dataLabel)) {
				return createBoundedValue(dataId, dataLabel);
			}
		}
		
		return null;
	}

}
