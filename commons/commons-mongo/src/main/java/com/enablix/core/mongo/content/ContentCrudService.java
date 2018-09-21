package com.enablix.core.mongo.content;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;

import com.enablix.core.mongo.search.SearchFilter;
import com.enablix.core.mongo.view.MongoDataView;

public interface ContentCrudService {

	void insert(String collectionName, String jsonData);
	
	void insert(String collectionName, Map<String, Object> data);
	
	Map<String, Object> findRecord(String collectionName, String elementIdentity, MongoDataView view);
	
	Map<String, Object> findRecord(String collectionName, String elementQId, String elementIdentity, MongoDataView view);
	
	Map<String, Object> findRecordByDocIdentity(String collectionName, String docElemQId, String docIdentity, MongoDataView view);
	
	List<Map<String, Object>> findChildElements(String collectionName, String childFieldId, String recordIdentity, MongoDataView view);
	
	List<Map<String, Object>> findAllRecord(String collectionName, MongoDataView view);
	
	Page<Map<String, Object>> findAllRecord(String collectionName, Pageable pageable, MongoDataView view);
	
	List<Map<String, Object>> findAllRecordWithParentId(String collectionName, String parentIdentity, MongoDataView view);
	
	Page<Map<String, Object>> findAllRecordWithParentId(String collectionName, String parentIdentity, Pageable pageable, MongoDataView view);
	
	List<Map<String, Object>> findAllRecordWithLinkContainerId(String collectionName, 
			String linkContentItemId, String linkContainerIdentity, MongoDataView view);
	
	Page<Map<String, Object>> findAllRecordWithLinkContainerId(String collectionName, 
			String linkContentItemId, String linkContainerIdentity, Pageable pageable, MongoDataView view);
	
	List<Map<String, Object>> findAllRecordForCriteria(String collectionName, Criteria criteria, MongoDataView view);
	
	Page<Map<String, Object>> findRecordsForCriteria(String collectionName, Criteria criteria, Pageable pageable, MongoDataView view);

	void updateAttributes(String collectionName, String elementQId, String elementIdentity, Map<String, Object> data);
	
	void updateBoundedLabel(String collectionName, String boundedAttrId, String boundedAttrIdValue, String newAttrLabelValue);
	
	void deleteBoundedItem(String collection, String boundedAttrId, String boundedAttrIdValue);

	void insertChildContainer(String collectionName, String parentIdentity, String childQId,
			Map<String, Object> data);
	
	Map<String, Object> deleteRecord(String collectionName, String recordIdentity);
	
	void deleteChild(String collectionName, String childQId, String childIdentity);
	
	List<String> deleteAllChild(String collectionName, String recordIdentity, String childQId);
	
	@SuppressWarnings("rawtypes")
	List<HashMap> deleteRecordsWithParentId(String collectionName, String parentIdentity);

	void upsert(String collectionName, String elementIdentity, Map<String, Object> data);

	Map<String, Object> findContainingRecord(String collectionName, String childQId, String childIdentity, MongoDataView view);

	List<Map<String, Object>> findRecords(String collectionName, List<String> elementIdentities, MongoDataView view);
	
	Page<Map<String, Object>> findRecords(String collectionName, List<String> elementIdentities, Pageable pageable, MongoDataView view);
	
	List<Map<String, Object>> findRecords(String collectionName, SearchFilter filter, MongoDataView view);

	Page<Map<String, Object>> findChildElements(String collName, String qIdRelativeToParent,
			String parentRecordIdentity, Pageable pageable, MongoDataView view);

	long findRecordCountWithLinkContainerId(String collectionName, String linkContentItemId,
			String linkContainerIdentity, MongoDataView view);

	long findRecordCountWithParentId(String collName, String recordIdentity, MongoDataView view);

	long findChildElementsCount(String collName, String qIdRelativeToParent, String recordIdentity, MongoDataView view);

	void updateContentStackLabel(String collectionName, String contentStackAttrId, String contentIdentity,
			String newContentLabel);

	void deleteContentStackItem(String collectionName, String contentStackAttrId, String contentIdentity);

	Long getRecordCount(String collectionName);

	boolean archiveRecord(String contentQId, String recordIdentity);

	boolean unarchiveRecord(String contentQId, String recordIdentity);

}
