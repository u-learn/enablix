package com.enablix.analytics.info.detection;

import java.util.Map;

public interface Information {

	String source();
	
	String type();
	
	Map<String, Object> infoData();
	
}
