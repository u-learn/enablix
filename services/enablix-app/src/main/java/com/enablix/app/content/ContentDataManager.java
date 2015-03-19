package com.enablix.app.content;

import com.enablix.app.content.update.UpdateContentRequest;

public interface ContentDataManager {

	void saveData(UpdateContentRequest request);
	
	String fetchDataJson(FetchContentRequest request);
	
	void deleteData(String templateId, String containerQId, String dataIdentity);
	
}
