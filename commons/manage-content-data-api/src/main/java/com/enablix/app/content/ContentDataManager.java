package com.enablix.app.content;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;

import com.enablix.app.content.delete.DeleteContentRequest;
import com.enablix.app.content.fetch.FetchContentRequest;
import com.enablix.app.content.update.UpdateContentRequest;
import com.enablix.core.api.ContentDataRecord;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.api.ContentRecordGroup;
import com.enablix.core.api.ContentStackItem;
import com.enablix.core.api.TemplateFacade;
import com.enablix.data.view.DataView;

public interface ContentDataManager {

	Map<String, Object> saveData(UpdateContentRequest request);
	
	Object fetchDataJson(FetchContentRequest request, DataView view);
	
	void deleteData(DeleteContentRequest request);

	List<Map<String, Object>> fetchPeers(FetchContentRequest request, DataView view);

	Map<String, Object> fetchParentRecord(TemplateFacade template, 
			String recordQId, Map<String, Object> record, DataView view);
	
	Map<String, Object> getContentRecord(ContentDataRef dataRef, TemplateFacade template, DataView view);
	
	List<Map<String, Object>> getContentRecords(String containerQId, List<String> recordIdentities, TemplateFacade template, DataView view);

	List<ContentDataRecord> getContentStackRecords(List<ContentStackItem> contentStackItems, DataView view);

	List<ContentRecordGroup> getContentStackForContentRecord(String containerQId, String instanceIdentity, Pageable pageable, DataView view);

	List<ContentRecordGroup> fetchAllChildrenData(String parentQId, String parentIdentity, Pageable pageable, DataView view);

	List<ContentRecordGroup> fetchRecordAndChildData(String contentQId, String contentIdentity, Pageable childPagination, DataView view);

	List<ContentRecordGroup> getContentStackItemForContentRecord(String containerQId, String instanceIdentity,
			String itemQId, Pageable pageable, DataView userDataView);
	
}
