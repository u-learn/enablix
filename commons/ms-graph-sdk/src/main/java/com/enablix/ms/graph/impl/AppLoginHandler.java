package com.enablix.ms.graph.impl;

import java.net.URI;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.enablix.ms.graph.MSGraphException;
import com.enablix.ms.graph.MSGraphSession;
import com.enablix.ms.graph.MSGraphUtil;
import com.enablix.ms.graph.model.OAuth20Token;

@Component
public class AppLoginHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(AppLoginHandler.class);
	
	private final String LOGIN_URL = "https://login.microsoftonline.com/%s/oauth2/v2.0/token";
	
	@Autowired
	private RestTemplate restTemplate;
	
	public MSGraphSession login(String clientId, String clientSecret, String orgId, String driveOwnerId) throws MSGraphException {
		
		String loginUrl = String.format(LOGIN_URL, orgId);
		
		MultiValueMap<String, String> headers = new HttpHeaders();
		headers.add("content-type", "application/x-www-form-urlencoded");
		
		String requestBody = "client_id=" + clientId + "&client_secret=" + clientSecret + 
				"&scope=https%3A%2F%2Fgraph.microsoft.com%2F.default" + "&grant_type=client_credentials";
		
		RequestEntity<String> requestEntity = null;
		
		OAuth20Token oauth20Token = null;
		
		try {
			
			requestEntity = new RequestEntity<String>(requestBody, headers, HttpMethod.POST, new URI(loginUrl));
			ResponseEntity<OAuth20Token> responseEntity = restTemplate.exchange(requestEntity, OAuth20Token.class);
			oauth20Token = responseEntity.getBody();
			
		} catch (URISyntaxException e) {
			
			MSGraphUtil.logAndThrowMSGraphException(LOGGER, "Unable to login", e);
			
		} catch (HttpStatusCodeException e) {
			MSGraphUtil.logAndThrowMSGraphException(LOGGER, "Unable to login", e);
		}
		
		return new MSGraphOAuth20AppSession(oauth20Token, orgId, driveOwnerId);
	}
	
	public static AppLoginHandler create(RestTemplate restTemplate) {
		AppLoginHandler handler = new AppLoginHandler();
		handler.restTemplate = restTemplate;
		return handler;
	}
	
}
