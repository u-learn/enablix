package com.enablix.app.content;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.springframework.data.domain.Pageable;

import com.enablix.app.content.delete.DeleteContentRequest;
import com.enablix.app.content.fetch.FetchContentRequest;
import com.enablix.app.content.update.UpdateContentRequest;
import com.enablix.app.content.update.UpdateContentResponse;
import com.enablix.core.api.ContentDataRecord;
import com.enablix.core.api.ContentRecordGroup;
import com.enablix.core.api.ContentRecordRef;
import com.enablix.core.api.ContentStackItem;
import com.enablix.core.api.TemplateFacade;
import com.enablix.data.view.DataView;

public interface ContentDataManager {

	UpdateContentResponse saveData(UpdateContentRequest request);
	
	Object fetchDataJson(FetchContentRequest request, DataView view);
	
	void deleteData(DeleteContentRequest request);
	
	void deleteRecordDocuments(Map<String, Object> record, String containerQId) throws IOException;

	List<Map<String, Object>> fetchPeers(FetchContentRequest request, DataView view);

	Map<String, Object> fetchParentRecord(TemplateFacade template, 
			String recordQId, Map<String, Object> record, DataView view);
	
	Map<String, Object> getContentRecord(ContentRecordRef dataRef, TemplateFacade template, DataView view);
	
	Map<String, Object> getContentRecordByDocRef(String docIdentity, String docContentQId, DataView view);
	
	List<Map<String, Object>> getContentRecords(String containerQId, List<String> recordIdentities, TemplateFacade template, DataView view);

	List<ContentDataRecord> getContentStackRecords(List<ContentStackItem> contentStackItems, DataView view);

	List<ContentRecordGroup> getContentStackForContentRecord(String containerQId, String instanceIdentity, Pageable pageable, DataView view);

	List<ContentRecordGroup> fetchAllChildrenData(String parentQId, String parentIdentity, Pageable pageable, 
			List<String> childQIds, String textQuery, DataView view);

	List<ContentRecordGroup> fetchRecordAndChildData(String contentQId, String contentIdentity, Pageable childPagination, 
			List<String> childQIds, String textQuery, DataView view);

	List<ContentRecordGroup> getContentStackItemForContentRecord(String containerQId, String instanceIdentity,
			String itemQId, Pageable pageable, DataView userDataView);

	List<ContentRecordGroup> fetchStackDetails(List<Map<String, String>> stackValue, Pageable pageable,
			DataView dataView);
	
	<T> List<ContentDataRecord> getContentRecords(Iterable<T> itr, 
			Function<T, ContentRecordRef> dataRefTx, DataView view);
	
}
