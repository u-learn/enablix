package com.enablix.app.content;

import com.enablix.app.content.fetch.FetchContentRequest;
import com.enablix.app.content.update.UpdateContentRequest;

public interface ContentDataManager {

	void saveData(UpdateContentRequest request);
	
	Object fetchDataJson(FetchContentRequest request);
	
	void deleteData(String templateId, String containerQId, String dataIdentity);
	
}
