package com.enablix.app.content;

import java.util.List;
import java.util.Map;

import com.enablix.app.content.delete.DeleteContentRequest;
import com.enablix.app.content.fetch.FetchContentRequest;
import com.enablix.app.content.update.UpdateContentRequest;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;

public interface ContentDataManager {

	Map<String, Object> saveData(UpdateContentRequest request);
	
	Object fetchDataJson(FetchContentRequest request);
	
	void deleteData(DeleteContentRequest request);

	List<Map<String, Object>> fetchPeers(FetchContentRequest request);

	Map<String, Object> fetchParentRecord(ContentTemplate template, 
			String recordQId, Map<String, Object> record);
	
}
