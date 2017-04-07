package com.enablix.data.view.mongo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.enablix.commons.constants.AppConstants;
import com.enablix.core.domain.segment.DataSegment;
import com.enablix.core.mongo.view.MongoDataView;
import com.enablix.data.view.DataSegmentAttrFilter;

public class DefaultMongoDataView implements MongoDataView {

	private DataSegment dataSegment;
	
	private CollectionViewBuilder collViewBuilder;
	
	private Map<String, DefaultCollectionView<?>> collectionViews;
	
	public DefaultMongoDataView(DataSegment dataSegment, CollectionViewBuilder collViewBuilder) {
		
		super();
		
		this.dataSegment = dataSegment;
		this.collViewBuilder = collViewBuilder;
		this.collectionViews = new HashMap<>();
	}

	@Override
	public String datastoreType() {
		return AppConstants.MONGO_DATASTORE;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public DefaultCollectionView<?> getCollectionView(String collectionName) {
		
		DefaultCollectionView<?> collectionView = collectionViews.get(collectionName);
		
		if (collectionView == null) {
			
			boolean visible = collViewBuilder.isCollectionVisible(collectionName, dataSegment);
			List<DataSegmentAttrFilter> dsAttrFilters = collViewBuilder.createDataSegmentFilters(collectionName, dataSegment);

			AttributeMatcher attributeMatcher = collViewBuilder.attributeMatcher(collectionName);
			collectionView = new DefaultCollectionView(collectionName, visible, dsAttrFilters, attributeMatcher);
			collectionViews.put(collectionName, collectionView);
		}
		
		return collectionView;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> boolean isRecordVisible(String collectionName, T record) {
		DefaultCollectionView<T> collectionView = (DefaultCollectionView<T>) getCollectionView(collectionName);
		return collectionView.isRecordVisible(record);
	}

}
