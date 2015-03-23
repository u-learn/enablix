package com.enablix.core.mongo.content;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.commons.util.QIdUtil;
import com.enablix.commons.util.StringUtil;
import com.enablix.commons.util.json.JsonUtil;

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

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> findRecord(final String collectionName, 
			String elementQId, final String elementIdentity) {

		Query query = createIdentityQuery(elementQId, elementIdentity);
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

	private Query createIdentityQuery(String elementQId, String identity) {
		String elementIdentity = StringUtil.isEmpty(elementQId) ? ContentDataConstants.IDENTITY_KEY
				: elementQId + "." + ContentDataConstants.IDENTITY_KEY;
		return Query.query(Criteria.where(elementIdentity).is(identity));
	}

	@Override
	public void insertChildContainer(String collectionName, String parentIdentity, 
			String childQId, Map<String, Object> data) {
		
		String parentQId = QIdUtil.getParentQId(childQId);
		String childId = QIdUtil.getElementId(childQId);
		
		Query query = createIdentityQuery(parentQId, parentIdentity);
		
		String pushId = createArrayUpdateKey(parentQId, childId);
		Update push = new Update().push(pushId).value(data);
		mongoTemplate.upsert(query, push, collectionName);
	}
	
	private String createArrayUpdateKey(String parentId, String childId) {
		return StringUtil.isEmpty(parentId) ? childId : parentId + "." + ARR_POSITIONAL_OP + "." + childId;
	}
	
	@Override
	public void updateAttributes(String collectionName, String elementQId, 
			String elementIdentity, Map<String, Object> data) {
		
		Query query = createIdentityQuery(elementQId, elementIdentity);
		
		Update update = new Update();
		for (Map.Entry<String, Object> entry : data.entrySet()) {
			if (!ContentDataConstants.IDENTITY_KEY.equals(entry.getKey())) {
				String updateKey = createArrayUpdateKey(elementQId, entry.getKey());
				update.set(updateKey, entry.getValue());
			}
		}
		
		mongoTemplate.updateFirst(query, update, collectionName);
	}
	
}
