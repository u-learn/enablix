package com.enablix.core.mongo.content;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.commons.util.QIdUtil;
import com.enablix.commons.util.StringUtil;
import com.enablix.commons.util.json.JsonUtil;
import com.enablix.core.mongo.search.SearchFilter;
import com.mongodb.BasicDBObject;

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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Map<String, Object> findRecord(final String collectionName, 
			String elementQId, final String elementIdentity) {

		Query query = createIdentityQuery(elementQId, elementIdentity);
		setProjectedField(query, elementQId);
		
		Map<String, Object> result = mongoTemplate.findOne(query, HashMap.class, collectionName);
		
		if (result != null && !StringUtil.isEmpty(elementQId)) {
		
			// fetch the record from the map result 
			if (result.containsKey(elementQId)) {
			
				Object elementValue = result.get(elementQId);
				if (elementValue instanceof Collection) {
				
					Collection values = (Collection) elementValue;
					
					if (values.size() > 0) {
						Object firstValue = values.iterator().next();
						if (firstValue instanceof Map) {
							result = (Map) firstValue;
						}
					}
				}
			}
		}
		
		return result;
		
		/*return mongoTemplate.execute(collectionName, new CollectionCallback<String>() {

			@Override
			public String doInCollection(DBCollection collection) throws MongoException, DataAccessException {
				Query query = createRecordIdQuery(recordId);
				DBObject dbObj = collection.findOne(query.getQueryObject());
				return dbObj == null ? null : dbObj.toString();
			}
			
		});*/
	}

	private void setProjectedField(Query query, String fieldQId) {
		if (!StringUtil.isEmpty(fieldQId)) {
			query.fields().include(fieldQId + "." + ARR_POSITIONAL_OP);
		}
	}
	
	@Override
	public void insert(String collectionName, Map<String, Object> data) {
		mongoTemplate.insert(data, collectionName);
	}

	private Query createIdentityQuery(String elementQId, String identity) {
		return Query.query(createIdentityCriteria(elementQId, identity));
	}
	
	private Criteria createIdentityCriteria(String elementQId, String identity) {
		String elementIdentity = StringUtil.isEmpty(elementQId) ? ContentDataConstants.IDENTITY_KEY
				: elementQId + "." + ContentDataConstants.IDENTITY_KEY;
		return Criteria.where(elementIdentity).is(identity);
	}
	
	private Criteria createIdentityInCriteria(String elementQId, List<String> identities) {
		String elementIdentity = StringUtil.isEmpty(elementQId) ? ContentDataConstants.IDENTITY_KEY
				: elementQId + "." + ContentDataConstants.IDENTITY_KEY;
		return Criteria.where(elementIdentity).in(identities);
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
			if (!ContentDataConstants.IDENTITY_KEY.equals(entry.getKey())
					&& !ID_FLD.equals(entry.getKey())) {
				String updateKey = createArrayUpdateKey(elementQId, entry.getKey());
				update.set(updateKey, entry.getValue());
			}
		}
		
		mongoTemplate.updateFirst(query, update, collectionName);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> findRecord(String collectionName, String elementIdentity) {
		Query query = createIdentityQuery(null, elementIdentity);
		return mongoTemplate.findOne(query, HashMap.class, collectionName);
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<HashMap> findRecords(String collectionName, List<String> elementIdentities) {
		Query query = Query.query(createIdentityInCriteria(null, elementIdentities));
		return mongoTemplate.find(query, HashMap.class, collectionName);
	}

	@Override
	public List<Map<String, Object>> findAllRecord(String collectionName) {
		return findAllRecord(collectionName, null).getContent();
	}

	private Criteria createParentCriteria(String parentIdentity) {
		String criteriaKey = ContentDataConstants.ASSOCIATIONS_KEY + "." + ContentDataConstants.PARENT_ASSOCIATION
				+ "." + ContentDataConstants.RECORD_IDENTITY_KEY;
		return Criteria.where(criteriaKey).is(parentIdentity);
	}
	
	@Override
	public List<Map<String, Object>> findAllRecordWithParentId(String collectionName, String parentIdentity) {
		return findAllRecordWithParentId(collectionName, parentIdentity, null).getContent();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<Map<String, Object>> findChildElements(String collectionName, 
			String childFieldId, String recordIdentity) {
		
		Map<String, Object> parentRecord = findRecord(collectionName, recordIdentity);
		
		Object childRecords = null;
		if (parentRecord != null) {
			childRecords = parentRecord.get(childFieldId);
		}
		
		return childRecords instanceof List ? (List) childRecords : new ArrayList<>();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Map<String, Object> deleteRecord(String collectionName, String recordIdentity) {
		Query query = createIdentityQuery(null, recordIdentity);
		HashMap record = mongoTemplate.findOne(query, HashMap.class, collectionName);
		mongoTemplate.remove(query, collectionName);
		return record;
	}

	@Override
	public void deleteChild(String collectionName, String childQId, String childIdentity) {
		
		Criteria criteria = createIdentityCriteria(childQId, childIdentity);
		Query query = Query.query(criteria);
		
		Update removeArrElement = new Update().pull(childQId, 
				new BasicDBObject(ContentDataConstants.IDENTITY_KEY, childIdentity));
		
		// removes the element from the array
		mongoTemplate.updateFirst(query, removeArrElement, collectionName);
		
	}

	@Override
	public List<String> deleteAllChild(String collectionName, String recordIdentity, String childQId) {
		
		List<Map<String, Object>> children = findChildElements(collectionName, childQId, recordIdentity);
		
		List<String> childIds = new ArrayList<>();
		
		for (Map<String, Object> child : children) {
			String childIdentity = (String) child.get(ContentDataConstants.IDENTITY_KEY);
			childIds.add(childIdentity);
		}
		
		// set the child to empty array list
		Map<String, Object> updateData = new HashMap<>();
		updateData.put(childQId, new ArrayList<Object>());
		
		updateAttributes(collectionName, recordIdentity, null, updateData);
		
		return childIds;
	}

	@SuppressWarnings({ "rawtypes"})
	@Override
	public List<HashMap> deleteRecordsWithParentId(String collectionName, String parentIdentity) {

		Query query = Query.query(createParentCriteria(parentIdentity));
		query.fields().include(ContentDataConstants.IDENTITY_KEY);
		
		List<HashMap> records = mongoTemplate.find(query, HashMap.class, collectionName);
		
		mongoTemplate.remove(query, collectionName);
		return records;
	}

	@Override
	public void upsert(String collectionName, String elementIdentity, Map<String, Object> data) {
		
		Query query = createIdentityQuery(null, elementIdentity);
		
		Update update = new Update();
		for (Map.Entry<String, Object> entry : data.entrySet()) {
			if (!ContentDataConstants.IDENTITY_KEY.equals(entry.getKey())
					&& !ID_FLD.equals(entry.getKey())) {
				String updateKey = createArrayUpdateKey(null, entry.getKey());
				update.set(updateKey, entry.getValue());
			}
		}

		mongoTemplate.upsert(query, update, collectionName);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> findContainingRecord(String collectionName, 
			String childRelativeQId, String childIdentity) {
		Query query = createIdentityQuery(childRelativeQId, childIdentity);
		return mongoTemplate.findOne(query, HashMap.class, collectionName);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<Map<String, Object>> findAllRecordWithLinkContainerId(String collectionName, String linkContentItemId,
			String linkContainerIdentity) {

		Query query = Query.query(Criteria.where(linkContentItemId + ".id").is(linkContainerIdentity));
		
		List<Map<String, Object>> result = (List) mongoTemplate.find(query, HashMap.class, collectionName);

		return result;
	}

	@Override
	public Page<Map<String, Object>> findAllRecord(String collectionName, Pageable pageable) {
		Query query = new Query();
		return findRecords(query, collectionName, pageable);
	}

	@Override
	public Page<Map<String, Object>> findAllRecordWithParentId(String collectionName, String parentIdentity,
			Pageable pageable) {
		
		Query query = Query.query(createParentCriteria(parentIdentity));
		return findRecords(query, collectionName, pageable);
	}

	@Override
	public Page<Map<String, Object>> findAllRecordWithLinkContainerId(String collectionName, String linkContentItemId,
			String linkContainerIdentity, Pageable pageable) {

		Query query = Query.query(Criteria.where(linkContentItemId + ".id").is(linkContainerIdentity));
		return findRecords(query, collectionName, pageable);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Page<Map<String, Object>> findRecords(Query query, String collectionName, Pageable pageable) {
		
		long count = 0;
		
		if (pageable != null) {
			count = mongoTemplate.count(query, collectionName);
			query = query.with(pageable);
		}
		
		List<Map<String, Object>> list =  (List) mongoTemplate.find(query, HashMap.class, collectionName);
		
		return new PageImpl<Map<String, Object>>(list, pageable, count);
	}

	@Override
	public void updateBoundedLabel(String collectionName, String boundedAttrId, String boundedAttrIdValue,
			String newAttrLabelValue) {
		
		Query query = Query.query(Criteria.where(boundedAttrId + ".id").is(boundedAttrIdValue));
		
		String setId = boundedAttrId + "." + ARR_POSITIONAL_OP + ".label";
		Update set = new Update().set(setId, newAttrLabelValue);
		mongoTemplate.updateMulti(query, set, collectionName);
		
	}

	@Override
	public void deleteBoundedItem(String collectionName, String boundedAttrId, String boundedAttrIdValue) {
		Query query = Query.query(new Criteria());
		Update pull = new Update().pull(boundedAttrId, Collections.singletonMap("id", boundedAttrIdValue));
		mongoTemplate.updateMulti(query, pull, collectionName);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<Map<String, Object>> findAllRecordForCriteria(String collectionName, Criteria criteria) {
		Query query = Query.query(criteria);
		return (List) mongoTemplate.find(query, HashMap.class, collectionName);
	}

	@Override
	public List<Map<String, Object>> findRecords(String collectionName, SearchFilter filter) {
		return findAllRecordForCriteria(collectionName, filter.toPredicate(new Criteria()));
	}

	@Override
	public Page<Map<String, Object>> findChildElements(String collName, String qIdRelativeToParent,
			String parentRecordIdentity, Pageable pageable) {
		
		List<Map<String, Object>> childElements = findChildElements(collName, qIdRelativeToParent, parentRecordIdentity);
		
		int startFrom = pageable.getPageNumber() * pageable.getPageSize();
		int endAt = startFrom + pageable.getPageSize();
		
		if (startFrom >= childElements.size()) {
			childElements = new ArrayList<>();
		}
		
		if (endAt <= 0) {
			childElements = new ArrayList<>();
		}
		
		if (endAt >= childElements.size()) {
			endAt = childElements.size();
		}
		
		childElements = childElements.subList(startFrom, endAt);
		return new PageImpl<>(childElements, pageable, childElements.size());
	}
	
}
