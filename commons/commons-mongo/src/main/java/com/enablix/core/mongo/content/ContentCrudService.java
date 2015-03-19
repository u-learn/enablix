package com.enablix.core.mongo.content;

import java.util.Map;

public interface ContentCrudService {

	void insert(String collectionName, String jsonData);
	
	void insert(String collectionName, Map<String, Object> data);
	
	String findRecord(String collectionName, String recordId);

	void upsert(String collectionName, Map<String, Object> data, String recordId);
	
}
