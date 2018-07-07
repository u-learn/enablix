package com.enablix.dms.onedrive;

import java.io.IOException;
import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;

import com.enablix.commons.caching.api.CachingService;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.domain.config.Configuration;
import com.enablix.ms.graph.MSGraphException;
import com.enablix.ms.graph.MSGraphSDK;
import com.enablix.ms.graph.MSGraphSession;

@Component
public class AuthenticatedActionExecutor {

	private static final String ONE_DRIVE_SESSION_CACHE_KEY = "ONE_DRIVE_SESSION";

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticatedActionExecutor.class);
	
	@Autowired
	private CachingService cache;
	
	@Autowired
	private MSGraphSDK msGraphSDK;
	
	public <R> R loginAndExecute(Configuration docStoreConfig, 
			AuthenticatedOperation<MSGraphSession, R> operation) throws IOException {
		
		MSGraphSession msSession = null;
		
		Object session = cache.get(ProcessContext.get().getTenantId(), ONE_DRIVE_SESSION_CACHE_KEY);
		if (session instanceof MSGraphSession) {
			msSession = (MSGraphSession) session;
		}
		
		R response = null;
		
		if (msSession == null || msSession.expiresAt().before(Calendar.getInstance().getTime())) {
		
			response = freshLoginAndExecute(docStoreConfig, operation);
			
		} else {
			
			try {
				response = operation.execute(msSession);
				
			} catch(Exception io) {
				
				if (isAuthenticationError(io)) {
					LOGGER.error("One Drive authentication error. Trying with fresh login");
					response = freshLoginAndExecute(docStoreConfig, operation);
				} else {
					throw io;
				}
			}
		}
		
		return response;
	}
	
	private boolean isAuthenticationError(Exception e) {
		
		if (e instanceof HttpStatusCodeException) {
			
			return ((HttpStatusCodeException) e).getStatusCode() == HttpStatus.UNAUTHORIZED;
			
		} else if (e.getCause() != null) {
			
			if (e.getCause() instanceof HttpStatusCodeException) {
				
				return ((HttpStatusCodeException) e.getCause()).getStatusCode() == HttpStatus.UNAUTHORIZED;
				
			} else {
				
				Throwable cause = e.getCause().getCause();
				
				if (cause != null && cause instanceof HttpStatusCodeException) {
					return ((HttpStatusCodeException) cause).getStatusCode() == HttpStatus.UNAUTHORIZED;
				}
			}
		}
		
		return false;
	}
	
	private <R> R freshLoginAndExecute(Configuration docStoreConfig, 
			AuthenticatedOperation<MSGraphSession, R> operation) throws IOException {
		
		MSGraphSession session = login(docStoreConfig);
		return operation.execute(session);
	}
	
	public MSGraphSession login(Configuration config) throws IOException {
		
		MSGraphSession session;
		
		try {
			
			String clientId = OneDriveUtil.getAppId(config);
			String clientSecret = OneDriveUtil.getAppPassword(config);
			String orgId = OneDriveUtil.getDriveOrgId(config);
			String driveOwnerId = OneDriveUtil.getDriveOwnerId(config);
			
			session = msGraphSDK.loginAsApp(clientId, clientSecret, orgId, driveOwnerId);
			
			cache.put(ProcessContext.get().getTenantId(), ONE_DRIVE_SESSION_CACHE_KEY, session);
			
		} catch (MSGraphException e) {
			LOGGER.error("Unable to login", e);
			throw new IOException("One Drive login failed", e);
		}
		
		return session;
	}
	
	public static interface AuthenticatedOperation<T, R> {
		R execute(T t) throws IOException;
	}
	
}
