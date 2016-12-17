package com.enablix.commons.caching.api;

public interface CachingService {

	void put(String key, Object value);
	
	Object get(String key);
	
	void put(String tenantId, String key, Object value);
	
	Object get(String tenantId, String key);
	
}
