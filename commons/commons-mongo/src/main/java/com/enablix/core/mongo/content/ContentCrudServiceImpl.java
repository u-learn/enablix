package com.enablix.core.mongo.content;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.core.CollectionCallback;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.commons.util.StringUtil;
import com.enablix.commons.util.json.JsonUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoException;

@Component
public class ContentCrudServiceImpl implements ContentCrudService {

	public static final String ID_FLD = "_id";
	public static final String ARR_POSITIONAL_OP = "$";
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Override
	public void insert(final String collectionName, final String jsonData) {
		insert(collectionName, JsonUtil.jsonToMap(jsonData));
	}

	@Override
	public Map<String, Object> findRecord(final String collectionName, final String recordId) {
		Query query = createRecordIdQuery(recordId);
		return mongoTemplate.findOne(query, HashMap.class, collectionName);
		/*return mongoTemplate.execute(collectionName, new CollectionCallback<String>() {

			@Override
			public String doInCollection(DBCollection collection) throws MongoException, DataAccessException {
				Query query = createRecordIdQuery(recordId);
				DBObject dbObj = collection.findOne(query.getQueryObject());
				return dbObj == null ? null : dbObj.toString();
			}
			
		});*/
	}

	@Override
	public void insert(String collectionName, Map<String, Object> data) {
		mongoTemplate.insert(data, collectionName);
	}

	@Override
	public void upsert(String collectionName, Map<String, Object> data, final String recordId) {
		Query query = createRecordIdQuery(recordId);
		BasicDBObject updateDbObj = new BasicDBObject(data);
		mongoTemplate.upsert(query, Update.fromDBObject(updateDbObj), collectionName);
	}
	
	private Query createRecordIdQuery(final String recordId) {
		return Query.query(Criteria.where(ID_FLD).is(recordId));
	}

	@Override
	public void insertChildContainer(String collectionName, String recordId, 
			String relativeChildQId, Map<String, Object> data) {
		Query query = createRecordIdQuery(recordId);
		Update push = new Update().push(relativeChildQId).value(data);
		mongoTemplate.upsert(query, push, collectionName);
	}
	
	@Override
	public void updateAttributes(String collectionName, String recordId, 
			String relativeChildQId, String childIdentity, Map<String, Object> data) {
		
		Criteria criteria = Criteria.where(ID_FLD).is(recordId);
		
		boolean updatingChild = !StringUtil.isEmpty(relativeChildQId);
		if (updatingChild) {
			criteria = criteria.and(relativeChildQId + ContentDataConstants.QUALIFIED_ID_SEP 
									+ ContentDataConstants.IDENTITY_KEY).is(childIdentity);
		}
		
		Query query = Query.query(criteria);
		
		Update update = new Update();
		for (Map.Entry<String, Object> entry : data.entrySet()) {
			if (!ContentDataConstants.IDENTITY_KEY.equals(entry.getKey())) {
				String updateKey = updatingChild ? 
						relativeChildQId + "." + ARR_POSITIONAL_OP + "." + entry.getKey() 
						: entry.getKey();
				update.set(updateKey, entry.getValue());
			}
		}
		
		mongoTemplate.updateFirst(query, update, collectionName);
	}
	
}
