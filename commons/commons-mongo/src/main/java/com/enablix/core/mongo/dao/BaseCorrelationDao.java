package com.enablix.core.mongo.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.enablix.core.domain.BaseDocumentEntity;

public class BaseCorrelationDao {

	@Autowired
	private MongoTemplate mongoTemplate;
	
	public <T extends BaseDocumentEntity> List<T> findByCriteria(Criteria queryCriteria, Class<T> findType) {
		Query query = Query.query(queryCriteria);
		return mongoTemplate.find(query, findType);
	}
	
}
