package com.enablix.app.content;

public interface ContentDataManager {

	void saveData(String templateId, String dataItemQualifiedId, String dataJson);
	
	String fetchDataJson(String templateId, String dataItemQualifiedId);
	
	void deleteData(String templateId, String dataItemQualifiedId, String dataIdentity);
	
}
