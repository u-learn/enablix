package com.enablix.core.mongo.content;

public interface ContentCrudService {

	void insert(String collectionName, String jsonData);
	
	String findRecord(String collectionName, String recordId);
	
}
