package com.enablix.analytics.info.detection;

import java.util.Collection;

public interface InfoAnalyser {

	Collection<Opinion> analyse(Information info, InfoDetectionContext ctx);
	
	String name();
	
	AnalysisLevel level();
	
}
