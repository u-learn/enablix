package com.enablix.core.mongo.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Field;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.Assert;

import com.enablix.commons.util.StringUtil;
import com.enablix.commons.util.collection.CollectionUtil;

public abstract class BaseDao {

	protected abstract MongoTemplate getMongoTemplate();
	
	public <T> List<T> findByCriteria(Criteria queryCriteria, Class<T> findType) {
		return findByCriteria(queryCriteria, null, findType);
	}
	
	public <T> List<T> findByCriteria(Criteria queryCriteria, Class<T> findType, List<String> projectionFields) {
		return findByCriteria(queryCriteria, null, findType, projectionFields);
	}
	
	public <T> List<T> findByCriteria(
			Criteria queryCriteria, String collectionName, Class<T> findType) {
		
		Query query = Query.query(queryCriteria);
		
		return StringUtil.isEmpty(collectionName) ? getMongoTemplate().find(query, findType) 
				: getMongoTemplate().find(query, findType, collectionName);
	}
	
	public <T> List<T> findByCriteria(
			Criteria queryCriteria, String collectionName, Class<T> findType, List<String> projectionFields) {
		
		Query query = Query.query(queryCriteria);
		
		addProjectionFields(query, projectionFields);
		
		return StringUtil.isEmpty(collectionName) ? getMongoTemplate().find(query, findType) 
				: getMongoTemplate().find(query, findType, collectionName);
	}

	private void addProjectionFields(Query query, List<String> projectionFields) {
		
		if (CollectionUtil.isNotEmpty(projectionFields)) {
			Field queryFields = query.fields();
			for (String projectionField : projectionFields) {
				queryFields.include(projectionField);
			}
		}
	}

	public <T> Page<T> findByCriteria(
			Criteria queryCriteria, Class<T> findType, Pageable pageable) {
		return findByCriteria(queryCriteria, null, findType, pageable, null);
	}

	public <T> Page<T> findByCriteria(Criteria queryCriteria, 
			String collectionName, Class<T> findType, Pageable pageable) {
		return findByCriteria(queryCriteria, collectionName, findType, pageable, null);
	}
	
	public <T> Page<T> findByCriteria(Criteria queryCriteria, 
			String collectionName, Class<T> findType, Pageable pageable, List<String> projectionFields) {
		
		Assert.notNull(pageable, "Pageable is be null");
		
		Query query = Query.query(queryCriteria);
		
		addProjectionFields(query, projectionFields);
		
		MongoTemplate mongoTemplate = getMongoTemplate();
		
		long count = StringUtil.hasText(collectionName) ? mongoTemplate.count(query, collectionName) 
										: mongoTemplate.count(query, findType);
				
		query.with(pageable);
		
		List<T> content = StringUtil.isEmpty(collectionName) ? mongoTemplate.find(query, findType) 
				: mongoTemplate.find(query, findType, collectionName);

		return new PageImpl<>(content, pageable, count);
	}
	
	public void updateMulti(Query query, Update update, Class<?> entityClass) {
		getMongoTemplate().updateMulti(query, update, entityClass);
	}
	
}
