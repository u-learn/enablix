package com.enablix.core.mongo.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

public class BaseTenantDao extends BaseDao {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	protected MongoTemplate getMongoTemplate() {
		return mongoTemplate;
	}
	
}
