package com.enablix.analytics.info.detection;

import java.util.List;

public interface ContentSuggestionBuilder {

	List<ContentSuggestion> build(InfoDetectionContext ctx);
	
}
