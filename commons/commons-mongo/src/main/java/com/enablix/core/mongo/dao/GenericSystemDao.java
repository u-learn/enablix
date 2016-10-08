package com.enablix.core.mongo.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
public class GenericSystemDao extends BaseDao {

	@Autowired
	private MongoTemplate systemMongoTemplate;

	@Override
	protected MongoTemplate getMongoTemplate() {
		return systemMongoTemplate;
	}
	
}
