package com.enablix.core.mongo;

import java.util.List;

import org.springframework.data.authentication.UserCredentials;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.DocumentCallbackHandler;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.query.Query;

import com.enablix.core.mongo.view.CollectionView;
import com.enablix.core.mongo.view.MongoDataView;
import com.enablix.core.mongo.view.MongoDataViewOperations;
import com.mongodb.Mongo;

public class CustomMongoTemplate extends MongoTemplate {

	public CustomMongoTemplate(Mongo mongo, String databaseName) {
		super(mongo, databaseName);
	}

	public CustomMongoTemplate(Mongo mongo, String databaseName, UserCredentials userCredentials) {
		super(mongo, databaseName, userCredentials);
	}
	
	public CustomMongoTemplate(MongoDbFactory mongoDbFactory) {
		super(mongoDbFactory);
	}

	public CustomMongoTemplate(MongoDbFactory mongoDbFactory, MongoConverter mongoConverter) {
		super(mongoDbFactory, mongoConverter);
	}
	
	public void executeQuery(Query query, String collectionName, DocumentCallbackHandler dch) {
		
		CollectionView<?> collectionView = MongoDataView.ALL_DATA.getCollectionView(collectionName);
		query = query.addCriteria(collectionView.viewFilter());
		
		System.out.println("Custom query: " + query);
		
		super.executeQuery(query, collectionName, dch);
	}
	
	public <T> List<T> find(final Query query, Class<T> entityClass, String collectionName) {
		
		Query queryToExecute = query;
		
		MongoDataViewContext dataViewCtx = MongoDataViewContext.get();
		
		if (dataViewCtx != null) {
			MongoDataViewOperations dataViewOp = new MongoDataViewOperations(this, dataViewCtx.getView());
			queryToExecute = dataViewOp.getViewScopedQuery(query, collectionName);
		}
		
		return super.find(queryToExecute, entityClass, collectionName);
	}
	
	public long count(Query query, Class<?> entityClass, String collectionName) {
		
		Query queryToExecute = query;
		
		MongoDataViewContext dataViewCtx = MongoDataViewContext.get();
		
		if (dataViewCtx != null) {
			MongoDataViewOperations dataViewOp = new MongoDataViewOperations(this, dataViewCtx.getView());
			queryToExecute = dataViewOp.getViewScopedQuery(query, collectionName);
		}
		
		return super.count(queryToExecute, entityClass, collectionName);
	}
	
}
