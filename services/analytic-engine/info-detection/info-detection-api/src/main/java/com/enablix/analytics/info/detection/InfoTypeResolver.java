package com.enablix.analytics.info.detection;

import java.util.List;

public interface InfoTypeResolver {

	List<TypeOpinion> resolve(InfoDetectionContext ctx);
	
	String name();
	
}
