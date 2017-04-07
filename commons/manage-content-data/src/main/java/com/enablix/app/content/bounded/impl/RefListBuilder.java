package com.enablix.app.content.bounded.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import com.enablix.app.content.bounded.BoundedListBuilder;
import com.enablix.app.content.bounded.DataItem;
import com.enablix.commons.util.StringUtil;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.BoundedListDatastoreType;
import com.enablix.core.commons.xsdtopojo.BoundedRefListType;
import com.enablix.core.commons.xsdtopojo.BoundedType;
import com.enablix.core.commons.xsdtopojo.DatastoreLocationType;
import com.enablix.core.mongo.content.ContentCrudService;
import com.enablix.core.mongo.dao.BaseDao;
import com.enablix.core.mongo.dao.GenericDao;
import com.enablix.core.mongo.dao.GenericSystemDao;
import com.enablix.core.mongo.view.MongoDataView;
import com.enablix.data.view.DataView;
import com.enablix.services.util.DataViewUtil;

@Component
public class RefListBuilder implements BoundedListBuilder {

	@Autowired
	private ContentCrudService contentCrudService;
	
	@Autowired
	private GenericSystemDao systemDao;
	
	@Autowired
	private GenericDao tenantDao;
	
	@SuppressWarnings("unchecked")
	@Override
	public Collection<DataItem> buildBoundedList(TemplateFacade template, 
				BoundedType boundedTypeDef, DataView dataView) {
		
		Set<DataItem> itemList = new HashSet<>();
		
		BoundedRefListType refListType = boundedTypeDef.getRefList();
		BoundedListDatastoreType boundedListDS = refListType.getDatastore();
		String storeId = boundedListDS.getStoreId();
		
		DatastoreLocationType dsLocation = boundedListDS.getLocation();
		
		MongoDataView view = DataViewUtil.getMongoDataView(dataView);
		
		List<Map<String, Object>> records = null;
		
		switch(dsLocation) {

			case TENANT_DB:
				records = getDBCollectionRecords(storeId, tenantDao, view);
				break;
				
			case SYSTEM_DB:
				records = getDBCollectionRecords(storeId, systemDao, view);
				break;
				
			default:
				String collectionName = template.getCollectionName(boundedListDS.getStoreId());
				records = contentCrudService.findAllRecord(collectionName, view);
		}
		
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



	@SuppressWarnings({ "rawtypes" })
	private List getDBCollectionRecords(String storeId, BaseDao dao, MongoDataView view) {
		return dao.findByCriteria(new Criteria(), storeId, HashMap.class, view);
	}
	
	

	private String getStringValue(Object data) {
		return data == null ? null : String.valueOf(data);
	}
	
	@Override
	public boolean canHandle(BoundedType boundedType) {
		return boundedType.getRefList() != null;
	}

}
