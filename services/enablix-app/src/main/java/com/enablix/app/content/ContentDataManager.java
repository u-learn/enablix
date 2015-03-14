package com.enablix.app.content;

public interface ContentDataManager {

	void saveData(UpdateContentRequest request);
	
	String fetchDataJson(FetchContentRequest request);
	
	void deleteData(String templateId, String containerQId, String dataIdentity);
	
}
