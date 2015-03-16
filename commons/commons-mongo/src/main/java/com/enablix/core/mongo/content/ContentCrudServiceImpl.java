package com.enablix.core.mongo.content;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.core.CollectionCallback;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.stereotype.Component;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoException;

@Component
public class ContentCrudServiceImpl implements ContentCrudService {

	public static final String ID_FLD = "_id";
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Override
	public void insert(final String collectionName, final String jsonData) {
		mongoTemplate.insert(jsonData, collectionName);
	}

	@Override
	public String findRecord(final String collectionName, final String recordId) {

		return mongoTemplate.execute(collectionName, new CollectionCallback<String>() {

			@Override
			public String doInCollection(DBCollection collection) throws MongoException, DataAccessException {
				BasicQuery query = new BasicQuery("{" + ID_FLD + ":'" + recordId + "'}");
				DBObject dbObj = collection.findOne(query.getQueryObject());
				return dbObj == null ? null : dbObj.toString();
			}
			
		});
	}

	@Override
	public void insert(String collectionName, Map<String, Object> data) {
		mongoTemplate.insert(data, collectionName);
	}

}
