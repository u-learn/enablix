package com.enablix.core.mongo.content;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.core.CollectionCallback;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.stereotype.Component;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import com.mongodb.util.JSON;

@Component
public class ContentCrudServiceImpl implements ContentCrudService {

	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Override
	public void insert(final String collectionName, final String jsonData) {
		DBObject jsonDBObj = (DBObject) JSON.parse(jsonData);
		mongoTemplate.insert(jsonDBObj, collectionName);
	}

	@Override
	public String findRecord(final String collectionName, final String recordId) {

		return mongoTemplate.execute(collectionName, new CollectionCallback<String>() {

			@Override
			public String doInCollection(DBCollection collection) throws MongoException, DataAccessException {
				BasicQuery query = new BasicQuery("{_id:'" + recordId + "'}");
				DBObject dbObj = collection.findOne(query.getQueryObject());
				return dbObj == null ? null : dbObj.toString();
			}
			
		});
	}

}
