package com.enablix.analytics.info.detection;

import java.util.List;

public interface InfoDetector {

	List<ContentSuggestion> analyse(Information info);
	
	void analyseAndSaveContentRecord(Information info, boolean updateExistingRecord);
	
}
