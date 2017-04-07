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
import com.enablix.core.mongo.view.MongoDataView;
import com.enablix.core.mongo.view.MongoDataViewOperations;

public abstract class BaseDao {

	protected abstract MongoTemplate getMongoTemplate();
	
	public <T> List<T> findByCriteria(Criteria queryCriteria, Class<T> findType, MongoDataView view) {
		return findByCriteria(queryCriteria, null, findType, view);
	}
	
	public <T> List<T> findByCriteria(Criteria queryCriteria, Class<T> findType, 
			List<String> projectionFields, MongoDataView view) {
		return findByCriteria(queryCriteria, null, findType, projectionFields, view);
	}
	
	public <T> List<T> findByCriteria(
			Criteria queryCriteria, String collectionName, Class<T> findType, MongoDataView view) {
		
		MongoDataViewOperations viewOperations = new MongoDataViewOperations(getMongoTemplate(), view);
		Query query = viewOperations.getViewScopedQuery(queryCriteria, collectionName);
		
		return StringUtil.isEmpty(collectionName) ? viewOperations.find(query, findType) 
				: viewOperations.find(query, findType, collectionName);
	}
	
	public <T> List<T> findByCriteria(Criteria queryCriteria, String collectionName, 
			Class<T> findType, List<String> projectionFields, MongoDataView view) {
		
		MongoDataViewOperations viewOperations = new MongoDataViewOperations(getMongoTemplate(), view);
		
		boolean collNameMissing = StringUtil.isEmpty(collectionName);
		
		Query query = collNameMissing ? viewOperations.getViewScopedQuery(queryCriteria, findType)
				: viewOperations.getViewScopedQuery(queryCriteria, collectionName); 
		
		addProjectionFields(query, projectionFields);
		
		return collNameMissing ? viewOperations.find(query, findType) 
				: viewOperations.find(query, findType, collectionName);
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
			Criteria queryCriteria, Class<T> findType, Pageable pageable, MongoDataView view) {
		return findByCriteria(queryCriteria, null, findType, pageable, null, view);
	}

	public <T> Page<T> findByCriteria(Criteria queryCriteria, 
			String collectionName, Class<T> findType, Pageable pageable, MongoDataView view) {
		return findByCriteria(queryCriteria, collectionName, findType, pageable, null, view);
	}
	
	public <T> Page<T> findByCriteria(Criteria queryCriteria, String collectionName, 
			Class<T> findType, Pageable pageable, List<String> projectionFields, MongoDataView view) {
		
		Assert.notNull(pageable, "Pageable cannot be null");
		
		boolean collNameMissing = StringUtil.isEmpty(collectionName);
		
		MongoDataViewOperations viewOperations = new MongoDataViewOperations(getMongoTemplate(), view);
		Query query = collNameMissing ? viewOperations.getViewScopedQuery(queryCriteria, findType)
				: viewOperations.getViewScopedQuery(queryCriteria, collectionName);
		
		addProjectionFields(query, projectionFields);
		
		long count = StringUtil.hasText(collectionName) ? viewOperations.count(query, collectionName) 
										: viewOperations.count(query, findType);
				
		query.with(pageable);
		
		List<T> content = collNameMissing ? viewOperations.find(query, findType) 
				: viewOperations.find(query, findType, collectionName);

		return new PageImpl<>(content, pageable, count);
	}
	
	public void updateMulti(Query query, Update update, Class<?> entityClass) {
		getMongoTemplate().updateMulti(query, update, entityClass);
	}
	
}
