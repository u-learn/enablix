package com.enablix.core.mongo.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.Assert;

import com.enablix.commons.util.StringUtil;

public class BaseDao {

	@Autowired
	protected MongoTemplate mongoTemplate;
	
	public <T> List<T> findByCriteria(Criteria queryCriteria, Class<T> findType) {
		return findByCriteria(queryCriteria, findType);
	}
	
	public <T> List<T> findByCriteria(
			Criteria queryCriteria, String collectionName, Class<T> findType) {
		
		Query query = Query.query(queryCriteria);
		
		return StringUtil.isEmpty(collectionName) ? mongoTemplate.find(query, findType) 
				: mongoTemplate.find(query, findType, collectionName);
	}

	public <T> Page<T> findByCriteria(
			Criteria queryCriteria, Class<T> findType, Pageable pageable) {
		return findByCriteria(queryCriteria, null, findType, pageable);
	}
	
	public <T> Page<T> findByCriteria(Criteria queryCriteria, 
			String collectionName, Class<T> findType, Pageable pageable) {
		
		Assert.notNull(pageable, "Pageable is be null");
		
		Query query = Query.query(queryCriteria);
		
		long count = mongoTemplate.count(query, findType);
				
		query.with(pageable);
		
		List<T> content = StringUtil.isEmpty(collectionName) ? mongoTemplate.find(query, findType) 
				: mongoTemplate.find(query, findType, collectionName);

		return new PageImpl<>(content, pageable, count);
	}
	
}
