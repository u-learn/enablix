package com.enablix.core.mongo.content;

import java.util.Map;

public interface ContentCrudService {

	void insert(String collectionName, String jsonData);
	
	void insert(String collectionName, Map<String, Object> data);
	
	Map<String, Object> findRecord(String collectionName, String recordId);

	void upsert(String collectionName, Map<String, Object> data, String recordId);

	void insertChildContainer(String collectionName, String recordId, 
			String childQId, Map<String, Object> data);

	void updateAttributes(String collectionName, String recordId, 
			String relativeChildQId, String childIdentity, Map<String, Object> data);
	
}
