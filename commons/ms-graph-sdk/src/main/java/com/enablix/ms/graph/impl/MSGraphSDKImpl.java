package com.enablix.ms.graph.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.enablix.commons.util.StringUtil;
import com.enablix.commons.util.web.WebUtils;
import com.enablix.ms.graph.MSGraphException;
import com.enablix.ms.graph.MSGraphSDK;
import com.enablix.ms.graph.MSGraphSession;
import com.enablix.ms.graph.MSGraphUtil;
import com.enablix.ms.graph.model.OneDriveFile;
import com.enablix.ms.graph.model.OneDriveFolder;
import com.enablix.ms.graph.model.UploadSession;

@Component
public class MSGraphSDKImpl implements MSGraphSDK {

	private static final Logger LOGGER = LoggerFactory.getLogger(MSGraphSDKImpl.class);
	
	static {
		// HttpURLConnection class does not allow non-HTTP methods like PATCH
		// Using reflection to add it as an allowed method
        try {
        	
            Field methodsField = HttpURLConnection.class.getDeclaredField("methods");

            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(methodsField, methodsField.getModifiers() & ~Modifier.FINAL);

            methodsField.setAccessible(true);

            String[] oldMethods = (String[]) methodsField.get(null);
            Set<String> methodsSet = new LinkedHashSet<>(Arrays.asList(oldMethods));
            methodsSet.addAll(Arrays.asList("PATCH"));
            String[] newMethods = methodsSet.toArray(new String[0]);

            methodsField.set(null/*static field*/, newMethods);
            
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
	}
	
	@Autowired
	private AppLoginHandler appLogin;
	
	@Autowired
	private FileUploadHandler fileUploadHandler;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Override
	public MSGraphSession loginAsApp(String clientId, String clientSecret, String orgId, String driveOwnerId) throws MSGraphException {
		return appLogin.login(clientId, clientSecret, orgId, driveOwnerId);
	}

	@Override
	public OneDriveFile uploadFile(MSGraphSession msSession, String filePath, String filename,
			InputStream dataStream, long contentLength) throws MSGraphException {
		
		UploadSession uploadSession = createUploadSession(msSession, filePath, filename);
		return uploadFile(msSession, uploadSession, filePath, filename, dataStream, contentLength);
	}

	private OneDriveFile uploadFile(MSGraphSession msSession, UploadSession uploadSession, String filePath, String filename,
			InputStream dataStream, long contentLength) throws MSGraphException {
		
		OneDriveFile file = null;
		
		try {
			file = fileUploadHandler.upload(msSession, uploadSession, dataStream, contentLength);
		} catch (MSGraphException e) {
			logAndThrowMSGraphException("Error uploading file chunks for [" + filename + "]", e);
		}
		
		return file;
	}

	private UploadSession createUploadSession(MSGraphSession msSession, String filePath, String filename)
			throws MSGraphException {
		
		UploadSession uploadSession = null;
		
		filePath = removeSlashes(filePath);
		String itemPath = convertPathToApiPath(filePath + "/" + filename);
		String uploadSessionUri = msSession.getDriveBaseUrl() + "/" + itemPath + "createUploadSession";
		
		MultiValueMap<String, String> headers = msSession.commonHeaders();
		headers.set("Content-Type", "application/json");
		
		String body = "{\"item\": { \"@microsoft.graph.conflictBehavior\": \"replace\" } }";
		
		try {
			
			RequestEntity<String> requestEntity = new RequestEntity<String>(body, headers, HttpMethod.POST, new URI(uploadSessionUri));
			ResponseEntity<UploadSession> responseEntity = restTemplate.exchange(requestEntity, UploadSession.class);
			
			if (responseEntity.getStatusCode() != HttpStatus.OK) {
				LOGGER.error("Error creating upload session for file: {}", filename);
				throw new MSGraphException("Unable to create upload session for file [" + filename + "] in destination [" + filePath + "]");
			}
			
			uploadSession = responseEntity.getBody();
			
		} catch (Exception e) {
			logAndThrowMSGraphException("Error creating upload session for [" + filename + "] in destination [" + filePath + "]", e);
		}
		
		return uploadSession;
	}

	@Override
	public InputStream getFileStreamById(MSGraphSession msSession, String driveItemId) throws MSGraphException {

		String downloadFileUri = msSession.getDriveBaseUrl() + "/items/" + driveItemId + "/content";
		
		return downloadFile(msSession, driveItemId, downloadFileUri);
	}

	private InputStream downloadFile(MSGraphSession msSession, String filePointer, String downloadFileUri) throws MSGraphException {
		
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet request = new HttpGet(downloadFileUri);

		MultiValueMap<String, String> headerMap = msSession.commonHeaders();
		for (String header : headerMap.keySet()) {
			request.addHeader(header, headerMap.getFirst(header));
		}
		
		InputStream dataStream = null;
		CloseableHttpResponse response;
		
		try {
		
			response = httpClient.execute(request);
			
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == org.apache.http.HttpStatus.SC_NOT_FOUND) {
				LOGGER.error("File [{}] not found", filePointer);
				throw new MSGraphException("File not found");
			}
			
			if (statusCode == org.apache.http.HttpStatus.SC_UNAUTHORIZED) {
				
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				response.getEntity().writeTo(bos);
				String res = bos.toString();
				LOGGER.error("Authentication Failed: {}", res);
				
				throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "Authentication Failed");
			}
			
			dataStream = response.getEntity().getContent();
			
		} catch (IOException e) {
			
			logAndThrowMSGraphException(
					"Error downloading file [" + filePointer + "] from One Drive", e);
		}
		
		return dataStream;
	}

	@Override
	public InputStream getFileStreamByPath(MSGraphSession msSession, String fileLocation) throws MSGraphException {
		
		String downloadFileUri = msSession.getDriveBaseUrl() + "/" + convertPathToApiPath(fileLocation) + "content";
		
		return downloadFile(msSession, fileLocation, downloadFileUri);
	}

	@Override
	public OneDriveFolder moveFile(MSGraphSession msSession, String driveItemId, String moveToFilepath, String name)
			throws MSGraphException {
		
		OneDriveFolder folder = checkAndGetFolder(msSession, moveToFilepath);
		
		String moveURI = msSession.getDriveBaseUrl() + "/items/" + driveItemId;
		
		MultiValueMap<String, String> headers = msSession.commonHeaders();
		headers.set("Content-Type", "application/json");
		
		String body = "{\"parentReference\": { \"id\": \"" + folder.getId() + "\" }, \"@microsoft.graph.conflictBehavior\": \"replace\" }";
		
		try {
			
			RequestEntity<String> requestEntity = new RequestEntity<String>(body, headers, HttpMethod.PATCH, new URI(moveURI));
			ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);
			
			String response = responseEntity.getBody();
			
			if (responseEntity.getStatusCode() != HttpStatus.OK) {
				LOGGER.error("Error moving file: {}", response);
				throw new MSGraphException("Unable to move file [" + driveItemId + "] to destination [" + moveToFilepath + "]");
			}
			
		} catch (Exception e) {
			logAndThrowMSGraphException("Error moving file [" + driveItemId + "] to destination [" + moveToFilepath + "]", e);
		}
		
		return folder;
	}

	private OneDriveFolder checkAndGetFolder(MSGraphSession msSession, String folderPath) throws MSGraphException {
		
		folderPath = removeSlashes(folderPath);
		
		OneDriveFolder folder = getPathFolder(msSession, folderPath);
		
		if (folder == null) {
		
			LOGGER.debug("Folder [{}] does not exist, creating.", folderPath);
			
			String parentFolderId = null;
			
			String parentFolder = folderPath;
			String childFolder = folderPath;
			
			int lastIndexOf = parentFolder.lastIndexOf('/');
			
			if (lastIndexOf > 0) {
				
				parentFolder = folderPath.substring(0, lastIndexOf);
				childFolder = folderPath.substring(lastIndexOf + 1);
				
				OneDriveFolder parent = checkAndGetFolder(msSession, parentFolder);
				
				parentFolderId = parent.getId();
				
			}
			
			// create the folder now
			folder = createFolder(msSession, parentFolderId, childFolder);
		}
		
		return folder;
	}
	
	private OneDriveFolder createFolder(MSGraphSession msSession, 
			String parentFolderId, String folderName) throws MSGraphException {
		
		OneDriveFolder folder = null;
		String createURI = StringUtil.hasText(parentFolderId) ? 
				msSession.getDriveBaseUrl() + "/items/" + parentFolderId + "/children" :
				msSession.getDriveBaseUrl() + "/root/children" ;
		
		MultiValueMap<String, String> headers = msSession.commonHeaders();
		headers.set("Content-Type", "application/json");
		
		String body = "{\"name\": \"" + folderName + "\", \"folder\": { }, \"@microsoft.graph.conflictBehavior\": \"replace\" }";
		
		try {
			
			RequestEntity<String> requestEntity = new RequestEntity<String>(body, headers, HttpMethod.POST, new URI(createURI));
			ResponseEntity<OneDriveFolder> responseEntity = restTemplate.exchange(requestEntity, OneDriveFolder.class);
			
			if (responseEntity.getStatusCode() != HttpStatus.OK && 
					responseEntity.getStatusCode() != HttpStatus.CREATED) {
				LOGGER.error("Error creating folder: {}", folderName);
				throw new MSGraphException("Unable to create folder [" + folderName + "] in destination [" + parentFolderId + "]");
			}
			
			folder = responseEntity.getBody();
			
		} catch (Exception e) {
			logAndThrowMSGraphException("Error creating folder [" + folderName + "] in destination [" + parentFolderId + "]", e);
		}
		
		return folder;
	}
	
	private OneDriveFolder getPathFolder(MSGraphSession msSession, String folderPath) throws MSGraphException {
		
		String getItemURI = msSession.getDriveBaseUrl() + "/" + convertPathToApiPath(folderPath);
		
		MultiValueMap<String, String> headers = msSession.commonHeaders();
		
		OneDriveFolder folder = null;
		
		try {
			
			RequestEntity<Void> requestEntity = new RequestEntity<Void>(headers, HttpMethod.GET, new URI(getItemURI));
			ResponseEntity<OneDriveFolder> responseEntity = restTemplate.exchange(requestEntity, OneDriveFolder.class);
			
			if (responseEntity.getStatusCode() == HttpStatus.OK) {
				folder = responseEntity.getBody();
			}
			
		} catch (HttpStatusCodeException e) {
			// if its 404 error, then folder not existing. return null value
			if (e.getStatusCode() != HttpStatus.NOT_FOUND) {
				throw e;
			}
			
		} catch (Exception e) {
			logAndThrowMSGraphException("Unable to lookup folder [" + folderPath + "]", e);
		}
		
		return folder;
	}

	@Override
	public void deleteFile(MSGraphSession msSession, String driveItemId) throws MSGraphException {
		
		String deleteFileUrl = msSession.getDriveBaseUrl() + "/items/" + driveItemId;
		
		MultiValueMap<String, String> headers = msSession.commonHeaders();
		
		try {
			
			RequestEntity<Void> requestEntity = new RequestEntity<Void>(headers, HttpMethod.DELETE, new URI(deleteFileUrl));
			ResponseEntity<Void> responseEntity = restTemplate.exchange(deleteFileUrl, HttpMethod.DELETE, requestEntity, Void.class);
			
			if (responseEntity.getStatusCode() != HttpStatus.NO_CONTENT) {
				throw new MSGraphException("File Not found");
			}
			
		} catch (Exception e) {
			logAndThrowMSGraphException("Unable to delete file [" + driveItemId + "]", e);
		}
		
	}
	
	private void logAndThrowMSGraphException(String msg, Exception e) throws MSGraphException {
		MSGraphUtil.logAndThrowMSGraphException(LOGGER, msg, e);
	}
	
	/**
     * Converts a path to api specific path.
     * "/" -> "root/"
     * "/folder" -> "root:/folder:/"
     *
     * @param path
     * @return String
     */
    private String convertPathToApiPath(String path) {
    	
    	path = WebUtils.encodeURI(path);
    	
        if (path.equals("/")) {
            return "root/";
        } else {
            path = this.removeSlashes(path);
            return "root:/" + path + ":/";
        }
    }

    /**
     * Remove slashes from the beginning and the end.
     *
     * @param path
     * @return String
     */
    private String removeSlashes(String path) {
        if (path.charAt(0) == '/') {
            path = path.substring(1);
        }
        if (path.charAt(path.length() - 1) == '/') {
            path = path.substring(0, path.length() - 1);
        }

        return path;
    }
    
    public static MSGraphSDK create(AppLoginHandler loginHandler, RestTemplate restTemplate, FileUploadHandler fileUpload) {
    	MSGraphSDKImpl sdk = new MSGraphSDKImpl();
    	sdk.appLogin = loginHandler;
    	sdk.restTemplate = restTemplate;
    	sdk.fileUploadHandler = fileUpload;
    	return sdk;
    }

}
