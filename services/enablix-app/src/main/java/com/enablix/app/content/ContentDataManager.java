package com.enablix.app.content;

import java.util.Map;

import com.enablix.app.content.fetch.FetchContentRequest;
import com.enablix.app.content.update.UpdateContentRequest;

public interface ContentDataManager {

	Map<String, Object> saveData(UpdateContentRequest request);
	
	Object fetchDataJson(FetchContentRequest request);
	
	void deleteData(String templateId, String containerQId, String dataIdentity);
	
}
