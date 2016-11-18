package com.enablix.core.mongo;

import org.springframework.data.mongodb.MongoDbFactory;

public class MongoUtil {

	public static boolean isMultiTenantMongoDbFactory(MongoDbFactory mongoDbFactory) {
		return mongoDbFactory instanceof MultiTenantMongoDbFactory;
	}
	
}
