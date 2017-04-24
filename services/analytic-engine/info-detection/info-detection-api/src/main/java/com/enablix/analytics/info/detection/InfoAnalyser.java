package com.enablix.analytics.info.detection;

import java.util.List;

public interface InfoAnalyser {

	List<Opinion> analyse(Information info, InfoDetectionContext ctx);
	
	String name();
	
	AnalysisLevel level();
	
}
