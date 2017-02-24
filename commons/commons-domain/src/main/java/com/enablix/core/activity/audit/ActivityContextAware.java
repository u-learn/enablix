package com.enablix.core.activity.audit;

public interface ActivityContextAware {

	void setActivityOrigin(String activityOrigin);
	
	void setContextId(String contextId);

	void setContextName(String contextName);

	void setContextTerm(String contextTerm);
	
}
