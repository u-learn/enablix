package com.enablix.analytics.info.detection;

public interface InfoAnalyser {

	InfoDetectionContext analyse(InfoDetectionContext ctx);
	
	String name();
	
	AnalysisLevel level();
	
}
