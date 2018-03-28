package com.enablix.analytics.context;

public interface RequestContext {

	String templateId();
	
	String containerQId();
	
	String contentIdentity();
	
	int pageSize();
	
	int pageNo();
	
}
