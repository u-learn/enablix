package com.enablix.core.security;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LastRememberMeLoginCache {

	private static final Logger LOGGER = LoggerFactory.getLogger(LastRememberMeLoginCache.class);
	
	private Map<String, AtomicInteger> lastLoginCache;
	
	public LastRememberMeLoginCache() {
		lastLoginCache = new HashMap<>();
	}
	
	public int getAndSetLastRememberMeLoginDay(String username, int dayOfYear) {
		
		int previousLogin = -1;
		
		AtomicInteger lastLoginDay = lastLoginCache.get(username);
		
		if (lastLoginDay == null) {
		
			synchronized (lastLoginCache) {
				
				lastLoginDay = lastLoginCache.get(username);
				
				if (lastLoginDay == null) { // double checking required
				
					lastLoginDay = new AtomicInteger(dayOfYear);
					lastLoginCache.put(username, lastLoginDay);
					
				} else {
					previousLogin = lastLoginDay.getAndSet(dayOfYear);
				}
			}
			
		} else {
			previousLogin = lastLoginDay.getAndSet(dayOfYear);
		}
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Last user login: {}, {}", username, dayOfYear);
		}
		
		return previousLogin;
	}
	
}
