package com.enablix.core.api;

public interface DocInfo extends ContentLengthAwareDocument {

	String getLocation();

	void setLocation(String location);
	
	String getName();
	
	void setContentLength(long contentLength);
	
	String getContentType();

}
