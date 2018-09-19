package com.enablix.analytics.info.detection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface InfoDetectionConfiguration {

	default public String getDefaultContentTypeQId() {
		return null;
	}
	
	// for Information which is link aware, this will be used to identify 
	// the type of information from the URL (link) pattern of the information
	default public Map<String, String> getLinkPatternToContQId() {
		return new HashMap<>();
	}
	
	// Aliases for a tag
	default public Map<String, List<InfoTag>> getTagAliases() {
		return new HashMap<>();
	}
	
	default public boolean isSaveAsDraft() {
		return false;
	}
	
	InfoDetectionConfiguration NO_CONFIG = new InfoDetectionConfiguration() { };

	default public List<String> retainExistingAttrOnUpdate(String forContainerQId) {
		return null;
	}
	
}
