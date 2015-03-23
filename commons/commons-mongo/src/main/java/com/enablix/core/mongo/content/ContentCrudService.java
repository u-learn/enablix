package com.enablix.core.mongo.content;

import java.util.Map;

public interface ContentCrudService {

	void insert(String collectionName, String jsonData);
	
	void insert(String collectionName, Map<String, Object> data);
	
	Map<String, Object> findRecord(String collectionName, String elementQId, String elementIdentity);

	void updateAttributes(String collectionName, String elementQId, String elementIdentity, Map<String, Object> data);

	void insertChildContainer(String collectionName, String parentIdentity, String childQId,
			Map<String, Object> data);
	
}
