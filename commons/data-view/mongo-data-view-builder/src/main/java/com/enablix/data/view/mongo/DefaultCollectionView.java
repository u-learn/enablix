package com.enablix.data.view.mongo;

import java.util.List;

import org.springframework.data.mongodb.core.query.Criteria;

import com.enablix.core.mongo.search.SearchFilter;
import com.enablix.core.mongo.view.CollectionView;
import com.enablix.data.view.DataSegmentAttrFilter;

public class DefaultCollectionView<T> implements CollectionView<T> {

	private String collectionName;
	
	private boolean visible;
	
	private List<DataSegmentAttrFilter> filters;
	
	private AttributeMatcher<T> attrMatcher;
	
	private Criteria viewFilter;
	
	public DefaultCollectionView(String collectionName, boolean visible, 
			List<DataSegmentAttrFilter> filters, AttributeMatcher<T> attrMatcher) {
		
		this.collectionName = collectionName;
		this.visible = visible;
		this.filters = filters;
		this.attrMatcher = attrMatcher;
	}
	
	private void initViewFilter() {
		SearchFilter searchFilter = attrMatcher.attributeFilters(filters);
		viewFilter = searchFilter == null ? null : searchFilter.toPredicate(new Criteria());
	}
	
	@Override
	public boolean isVisible() {
		return visible;
	}

	@Override
	public Criteria viewFilter() {
		
		if (viewFilter == null) {
			initViewFilter();
		}
		
		return viewFilter;
	}

	@Override
	public boolean isRecordVisible(T record) {
		
		boolean visible = false;
		
		if (isVisible()) {
			visible = attrMatcher.matchAttributes(record, filters);
		}
		
		return visible;
	}

	@Override
	public String collectionName() {
		return collectionName;
	}

}
