package com.enablix.core.api;

public interface RecordReference {

	void setItemIdentity(String itemIdentity);
	
	void setItemTitle(String itemTitle);
	
	void setContainerQId(String containerQId);
	
	String getItemIdentity();
	
	String getItemTitle();
	
	String getContainerQId();
	
}
