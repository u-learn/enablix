package com.enablix.core.mongo.view;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.ReflectionUtils;

public class MongoDataViewOperations {

	private static final Logger LOGGER = LoggerFactory.getLogger(MongoDataViewOperations.class);
	
	private static final Criteria EMPTY_CRITERIA = new Criteria();
	
	private MongoOperations mongoOperations;
	
	private MongoDataView mongoDataView;

	public MongoDataViewOperations(MongoOperations mongoOperations, MongoDataView mongoDataView) {
		super();
		this.mongoOperations = mongoOperations;
		this.mongoDataView = mongoDataView;
	}
	
	public Query getViewScopedQuery(Criteria criteria, String collName) {
		
		boolean isQueryCriteriaEmpty = criteria.equals(EMPTY_CRITERIA);
		
		Query query = Query.query(criteria);
		
		CollectionView<?> collView = mongoDataView.getCollectionView(collName);
		
		if (collView != null) {
			
			Criteria baseCriteria = collView.viewFilter();
			
			if (baseCriteria != null) {
			
				boolean isBaseCriteriaEmpty = baseCriteria.equals(EMPTY_CRITERIA);
				
				if (!isBaseCriteriaEmpty) {
					
					if (isQueryCriteriaEmpty) {
						query = Query.query(baseCriteria);
					} else {
						query = Query.query(new Criteria().andOperator(criteria, baseCriteria));
					}
				}
			}
			
		}
		
		LOGGER.debug("Collection: {}, Data view query: {}", collName, query);
		
		return query;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Query getViewScopedQuery(Query query, String collName) {
		
		CollectionView collectionView = mongoDataView.getCollectionView(collName);
		Criteria viewFilter = collectionView.viewFilter();
		
		if (viewFilter == null) {
			return query;
		}
		
		if (query == null) {
			
			query = Query.query(viewFilter);
			
		} else {
			
			LOGGER.debug("########################## Collection: {}, Original query: {}", collName, query);
			
			Field criteriaField = ReflectionUtils.findField(query.getClass(), "criteria");
			criteriaField.setAccessible(true);
			
			try {
				
				Map criteriaMap = (Map) criteriaField.get(query);
				Object object = criteriaMap.get(null);
				
				if (object != null) {
					criteriaMap.put(null, viewFilter);
				} else {
					query.addCriteria(viewFilter);
				}
				
			} catch (IllegalArgumentException | IllegalAccessException e) {
				LOGGER.error("Error getting private criteria field", e);
				throw new RuntimeException(e);
			}
			
			LOGGER.debug("########################## Collection: {}, Custom query: {}", collName, query);
		}
		
		return query;
	}
	
	public <T> T findOne(Query query, Class<T> entityClass, String collectionName) {
		return isCollectionVisible(collectionName) ? 
				mongoOperations.findOne(query, entityClass, collectionName) : null;
	}
	
	public <T> List<T> find(final Query query, Class<T> entityClass, String collectionName) {
		return isCollectionVisible(collectionName) ? 
				mongoOperations.find(query, entityClass, collectionName) : new ArrayList<T>();
	}
	
	public <T> List<T> find(Query query, Class<T> entityClass) {
		return isCollectionVisible(entityClass) ? 
				mongoOperations.find(query, entityClass) : new ArrayList<T>();
	}
	
	public long count(final Query query, String collectionName) {
		return isCollectionVisible(collectionName) ? mongoOperations.count(query, collectionName) : 0;
	}
	
	public long count(Query query, Class<?> entityClass) {
		return isCollectionVisible(entityClass) ? mongoOperations.count(query, entityClass) : 0;
	}

	public <T> Query getViewScopedQuery(Criteria queryCriteria, Class<T> findType) {
		String collectionName = mongoOperations.getCollectionName(findType);
		return getViewScopedQuery(queryCriteria, collectionName);
	}
	
	private boolean isCollectionVisible(String collName) {
		CollectionView<?> collView = mongoDataView.getCollectionView(collName);
		return collView == null || collView.isVisible();
	}
	
	private boolean isCollectionVisible(Class<?> entityClass) {
		String collectionName = mongoOperations.getCollectionName(entityClass);
		return isCollectionVisible(collectionName);
	}

}
