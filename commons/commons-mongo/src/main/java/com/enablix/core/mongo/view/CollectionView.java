package com.enablix.core.mongo.view;

import org.springframework.data.mongodb.core.query.Criteria;

public interface CollectionView<T> {

	boolean isVisible();
	
	Criteria viewFilter();
	
	boolean isRecordVisible(T record);
	
	String collectionName();
	
}
