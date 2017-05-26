package com.enablix.analytics.info.detection;

import java.util.List;

public interface InfoDetectorSIGateway {

	public List<ContentSuggestion> analyse(InfoDetectionContext ctx);
	
}
