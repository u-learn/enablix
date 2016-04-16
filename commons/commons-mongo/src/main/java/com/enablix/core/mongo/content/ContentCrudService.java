package com.enablix.core.mongo.content;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ContentCrudService {

	void insert(String collectionName, String jsonData);
	
	void insert(String collectionName, Map<String, Object> data);
	
	Map<String, Object> findRecord(String collectionName, String elementIdentity);
	
	Map<String, Object> findRecord(String collectionName, String elementQId, String elementIdentity);
	
	List<Map<String, Object>> findChildElements(String collectionName, String childFieldId, String recordIdentity);
	
	List<Map<String, Object>> findAllRecord(String collectionName);
	
	Page<Map<String, Object>> findAllRecord(String collectionName, Pageable pageable);
	
	List<Map<String, Object>> findAllRecordWithParentId(String collectionName, String parentIdentity);
	
	Page<Map<String, Object>> findAllRecordWithParentId(String collectionName, String parentIdentity, Pageable pageable);
	
	List<Map<String, Object>> findAllRecordWithLinkContainerId(String collectionName, 
			String linkContentItemId, String linkContainerIdentity);
	
	Page<Map<String, Object>> findAllRecordWithLinkContainerId(String collectionName, 
			String linkContentItemId, String linkContainerIdentity, Pageable pageable);

	void updateAttributes(String collectionName, String elementQId, String elementIdentity, Map<String, Object> data);
	
	void updateBoundedLabel(String collectionName, String boundedAttrId, String boundedAttrIdValue, String newAttrLabelValue);
	
	void deleteBoundedItem(String collection, String boundedAttrId, String boundedAttrIdValue);

	void insertChildContainer(String collectionName, String parentIdentity, String childQId,
			Map<String, Object> data);
	
	void deleteRecord(String collectionName, String recordIdentity);
	
	void deleteChild(String collectionName, String childQId, String childIdentity);
	
	List<String> deleteAllChild(String collectionName, String recordIdentity, String childQId);
	
	List<String> deleteRecordsWithParentId(String collectionName, String parentIdentity);

	void upsert(String collectionName, String elementIdentity, Map<String, Object> data);

	Map<String, Object> findContainingRecord(String collectionName, String childQId, String childIdentity);
	
}
