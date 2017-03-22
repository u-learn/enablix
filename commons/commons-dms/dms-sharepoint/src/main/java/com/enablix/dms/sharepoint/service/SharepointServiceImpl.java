package com.enablix.dms.sharepoint.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.enablix.commons.util.StringUtil;
import com.enablix.commons.util.json.JsonUtil;
import com.enablix.core.domain.config.Configuration;
import com.enablix.dms.sharepoint.SharepointAPI;
import com.enablix.dms.sharepoint.SharepointConstants;
import com.enablix.dms.sharepoint.SharepointException;

@Component
public class SharepointServiceImpl implements SharepointService {

	private static final Logger LOGGER = LoggerFactory.getLogger(SharepointServiceImpl.class);
	
	private static final String LOGIN_TYPE_KEY = "LOGIN_TYPE";

	@Autowired
	private SharepointLoginHandlerFactory loginHandleFactory;
	
	@Autowired
	private SharepointAPI spAPI;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Override
	public SharepointSession login(Configuration config) throws SharepointException {
		
		String loginType = config.getStringValue(LOGIN_TYPE_KEY);
		if (StringUtil.isEmpty(loginType)) {
			loginType = SharepointConstants.LOGIN_TYPE_SP_ONLINE;
		}
		
		SharepointLoginHandler loginHandler = loginHandleFactory.getLoginHandler(loginType);
		return loginHandler.login(config);
	}

	@Override
	public void createFolder(SharepointSession session, String folderPath) throws SharepointException {
		try {
			checkAndCreateRecursiveFolders(session, folderPath);
		} catch (URISyntaxException e) {
			logAndThrowSharepointException("Unable to create folder [" + folderPath + "]", e);
		}
	}
	
	private String getFolderProperties(SharepointSession session, String folderPath) 
			throws URISyntaxException, SharepointException {
		
		MultiValueMap<String, String> headers = createRequestHeaderMap(session);
		headers.add("Content-type", "application/json;odata=verbose");
		headers.add("Accept", "application/json;odata=verbose");
		  
		String getFolderUri = spAPI.getGetFolderByServerRelativePathUrl(session.getSiteUrl(), folderPath);

		RequestEntity<String> request = new RequestEntity<>(headers, HttpMethod.GET, new URI(getFolderUri));
		String folderProps = null;
		
		try {
		
			ResponseEntity<String> response = restTemplate.exchange(request, String.class);
			folderProps = response.getBody();
			
		} catch (HttpClientErrorException | HttpServerErrorException e) {
			
			if (e.getStatusCode() == HttpStatus.NOT_FOUND || 
					e.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) { // Sharepoint throws 500 when folder path is incorrect
				// ignore and continue
			} else {
				LOGGER.error("Sharepoint response: {}", e.getResponseBodyAsString());
				logAndThrowSharepointException("Error fetching folder details: " + e.getMessage(), e);
			}
		}
		
		return folderProps;
	}
	
	

	private void checkAndCreateRecursiveFolders(SharepointSession session, String folderPath) 
			throws URISyntaxException, SharepointException {
		
		String folderProps = getFolderProperties(session, folderPath);
		
		if (StringUtil.isEmpty(folderProps)) {
		
			LOGGER.debug("Folder [{}] does not exist, creating.", folderPath);
			
			String parentFolder = folderPath;
			if (parentFolder.endsWith("/")) {
				parentFolder = parentFolder.substring(0, parentFolder.length() - 1);
			}
			
			parentFolder = parentFolder.substring(0, parentFolder.lastIndexOf('/'));
			
			checkAndCreateRecursiveFolders(session, parentFolder);

			// create the folder now
			createSharepointFolder(session, folderPath);
		}
		
	}

	private void createSharepointFolder(SharepointSession session, String folderPath) throws URISyntaxException {
		
		MultiValueMap<String, String> headers = createRequestHeaderMap(session);
		headers.add("Content-type", "application/json;odata=verbose");
		headers.add("Accept", "application/json;odata=verbose");
		
		String jsonBody = spAPI.getCreateFolderJson(folderPath);
		String createUri = spAPI.getCreateFolderUri(session.getSiteUrl());
		
		LOGGER.debug("Create folder path: {}", createUri);
		LOGGER.debug("Create folder json: {}", jsonBody);
		
		RequestEntity<String> requestEntity = new RequestEntity<String>(jsonBody, headers, HttpMethod.POST, new URI(createUri));
		
		restTemplate.exchange(requestEntity, String.class);
		
	}

	@Override
	public String uploadFile(SharepointSession session, String filepath, 
			String filename, InputStream dataStream) throws SharepointException {

		try {
			
			checkAndCreateRecursiveFolders(session, filepath);
			
		} catch (URISyntaxException e) {
			logAndThrowSharepointException("Error creating folders for file path [" + filepath + "]", e);
		}
		
		MultiValueMap<String, String> headers = createRequestHeaderMap(session);
		
		String path = spAPI.getPostFileUri(session.getSiteUrl(), filepath, filename);
		
		InputStreamResource isResource = new InputStreamResource(dataStream);
		
		String fileLocation = null; 
		RequestEntity<InputStreamResource> requestEntity;
		
		try {
		
			requestEntity = new RequestEntity<InputStreamResource>(isResource, headers, HttpMethod.POST, new URI(path));
			
			ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);
			fileLocation = extractFileLocationFromCreateFileResponse(responseEntity.getBody());
			
		} catch (URISyntaxException e) {
			logAndThrowSharepointException("Invalid create file URI [" + path + "]", e);
		}
		
		return fileLocation;
	}

	private String extractFileLocationFromCreateFileResponse(String createFileResponse) {
		return (String) JsonUtil.getJsonpathValue(createFileResponse, "ServerRelativeUrl");
	}

	private MultiValueMap<String, String> createRequestHeaderMap(SharepointSession session) {
		Map<String, String> reqHeaders = session.getRequestHeaders();
		
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		headers.setAll(reqHeaders);
		return headers;
	}

	@Override
	public InputStream getFileStream(SharepointSession spSession, String fileLocation)
			throws SharepointException {
		
		CloseableHttpClient httpClient = HttpClients.createDefault();
		String downloadFileUri = spAPI.getDownloadFileUri(spSession.getSiteUrl(), fileLocation);
		HttpGet request = new HttpGet(downloadFileUri);

		for (Map.Entry<String, String> header : spSession.getRequestHeaders().entrySet()) {
			request.addHeader(header.getKey(), header.getValue());
		}
		
		InputStream dataStream = null;
		CloseableHttpResponse response;
		
		try {
		
			response = httpClient.execute(request);
			if (response.getStatusLine().getStatusCode() == org.apache.http.HttpStatus.SC_NOT_FOUND) {
				LOGGER.error("File [{}] not found", fileLocation);
				throw new SharepointException("File not found");
				
			}
			
			dataStream = response.getEntity().getContent();
			
		} catch (IOException e) {
			
			logAndThrowSharepointException(
					"Error downloading file [" + fileLocation + "] from Sharepoint", e);
		}
		
		return dataStream;
	}

	private void logAndThrowSharepointException(String msg, Exception e) throws SharepointException {
		LOGGER.error(msg, e);
		throw new SharepointException(msg, e);
	}

	@Override
	public String moveFile(SharepointSession spSession, String currentFileLocation, String moveToFilepath, String filename)
			throws SharepointException {

		// ensure that the folder structure exists
		try {
			checkAndCreateRecursiveFolders(spSession, moveToFilepath);
		} catch (URISyntaxException e1) {
			logAndThrowSharepointException("Unable to check folder [" + moveToFilepath + "] structure", e1);
		}

		// now move the file
		String moveToFileLocation = moveToFilepath + "/" + filename;

		MultiValueMap<String, String> headers = createRequestHeaderMap(spSession);
		String path = spAPI.getMoveFileUri(spSession.getSiteUrl(), currentFileLocation, moveToFileLocation); 
		
		try {
			
			RequestEntity<String> requestEntity = new RequestEntity<String>(null, headers, HttpMethod.POST, new URI(path));
			restTemplate.exchange(requestEntity, String.class);
			
		} catch (URISyntaxException e) {
			logAndThrowSharepointException("Invalid move file URI [" + path + "]", e);
		}
		
		return moveToFileLocation;
	}

	@Override
	public void deleteFile(SharepointSession spSession, String fileLocation) throws SharepointException {
		
		MultiValueMap<String, String> headers = createRequestHeaderMap(spSession);
		headers.add("X-HTTP-Method", "DELETE");
		headers.add("IF-MATCH", "*");
		  
		String path = spAPI.getDeleteFileUri(spSession.getSiteUrl(), fileLocation); 
		
		try {
			
			RequestEntity<String> requestEntity = new RequestEntity<String>(null, headers, HttpMethod.POST, new URI(path));
			restTemplate.exchange(requestEntity, String.class);
			
		} catch (URISyntaxException e) {
			
			logAndThrowSharepointException("Invalid move file URI [" + path + "]", e);
			
		} catch (HttpClientErrorException e) {
			
			if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
				// ignore if not found
			} else {
				LOGGER.error("Sharepoint response: {}", e.getResponseBodyAsString());
				logAndThrowSharepointException("Unable to delete file [" + fileLocation + "]", e);
			}
		}
		
	}

}
