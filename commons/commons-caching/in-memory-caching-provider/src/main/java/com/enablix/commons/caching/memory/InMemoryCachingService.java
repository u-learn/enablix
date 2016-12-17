package com.enablix.commons.caching.memory;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.enablix.commons.caching.api.CachingService;

@Component
public class InMemoryCachingService implements CachingService {

	private final Map<String, Object> CACHE = new HashMap<>();

	@Override
	public void put(String key, Object value) {
		CACHE.put(key, value);
	}

	@Override
	public Object get(String key) {
		return CACHE.get(key);
	}

	@Override
	public void put(String tenantId, String key, Object value) {
		put(getTenantSpecificKey(tenantId, key), value);
	}

	@Override
	public Object get(String tenantId, String key) {
		return get(getTenantSpecificKey(tenantId, key));
	}
	
	private String getTenantSpecificKey(String tenantId, String key) {
		return tenantId + ":" + key;
	}
	
}
