package com.enablix.core.security;

import java.util.concurrent.TimeUnit;

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
	
	public RememberMeTokenArchive() {
		cache = CacheBuilder.newBuilder()
					.concurrencyLevel(5)
					.maximumSize(10000)
					.expireAfterWrite(30, TimeUnit.SECONDS)
					.build();
	}
	
	public void putToken(PersistentRememberMeToken archivedToken) {
		LOGGER.trace("Adding remember-me token to archive: {}", archivedToken.getTokenValue());
		cache.put(archivedToken.getTokenValue(), archivedToken);
	}
	
	public PersistentRememberMeToken getToken(String tokenId) {
		LOGGER.trace("Fetching remember-me token from archive: {}", tokenId);
		return cache.getIfPresent(tokenId);
	}
	
}
