package com.enablix.analytics.recommendation;

import com.enablix.analytics.context.RequestContext;
import com.enablix.analytics.context.UserContext;

public interface RecommendationContext {

	UserContext getUserContext();
	
	RequestContext getRequestContext();
	
}
