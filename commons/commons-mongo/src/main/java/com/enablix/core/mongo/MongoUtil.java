package com.enablix.core.mongo;

import org.springframework.data.mongodb.MongoDbFactory;

import com.enablix.core.mongo.view.MongoDataView;

public class MongoUtil {

	public static boolean isMultiTenantMongoDbFactory(MongoDbFactory mongoDbFactory) {
		return mongoDbFactory instanceof MultiTenantMongoDbFactory;
	}
	
	public static <T> T executeWithDataViewScope(
			MongoDataView dataView, DataViewOperation<T> operation) {
		
		T result = null;
		
		try {
			MongoDataViewContext.initialize(dataView);
			result = operation.execute();
		} finally {
			MongoDataViewContext.clear();
		}
		
		return result;
	}
	
	public static interface DataViewOperation<T> {
		T execute();
	}
	
}
