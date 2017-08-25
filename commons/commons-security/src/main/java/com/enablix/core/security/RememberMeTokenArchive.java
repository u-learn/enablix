package com.enablix.core.security;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.stereotype.Component;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

@Component
public class RememberMeTokenArchive {

	private static final Logger LOGGER = LoggerFactory.getLogger(RememberMeTokenArchive.class);
	
	private Cache<String, PersistentRememberMeToken> cache;
	
	private Map<String, AtomicInteger> lastLoginCache;
	
	public RememberMeTokenArchive() {
		
		cache = CacheBuilder.newBuilder()
					.concurrencyLevel(5)
					.maximumSize(10000)
					.expireAfterWrite(30, TimeUnit.SECONDS)
					.build();
		
		lastLoginCache = new HashMap<>();
	}
	
	public void putToken(PersistentRememberMeToken archivedToken) {
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("Adding remember-me token to archive: {}", archivedToken.getTokenValue());
		}
		cache.put(archivedToken.getTokenValue(), archivedToken);
	}
	
	public PersistentRememberMeToken getToken(String tokenId) {
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("Fetching remember-me token from archive: {}", tokenId);
		}
		return cache.getIfPresent(tokenId);
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
