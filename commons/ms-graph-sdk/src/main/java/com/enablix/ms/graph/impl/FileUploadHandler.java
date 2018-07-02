package com.enablix.ms.graph.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.enablix.commons.util.json.JsonUtil;
import com.enablix.ms.graph.MSGraphException;
import com.enablix.ms.graph.MSGraphSession;
import com.enablix.ms.graph.MSGraphUtil;
import com.enablix.ms.graph.model.OneDriveFile;
import com.enablix.ms.graph.model.UploadSession;

@Component
public class FileUploadHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(FileUploadHandler.class);
	
	private final int CHUNK_SIZE = 320 * 1024 * 30; // size multiple of 320 KiB
	
	@Autowired
	private RestTemplate restTemplate;
	
	public OneDriveFile upload(MSGraphSession msSession, UploadSession uploadSession, 
			InputStream is, long contentLength) throws MSGraphException {
		
		OneDriveFile file = null;
		
		try {
			
			byte[] buffer;
			long remainingBytes = contentLength;
			
			while (remainingBytes > 0) {
				
				buffer = (remainingBytes < CHUNK_SIZE) ? new byte[(int) remainingBytes] : new byte[CHUNK_SIZE];
				int readLength = is.read(buffer);
				
				file = uploadChunk(buffer, readLength, (contentLength - remainingBytes), contentLength, msSession, uploadSession);
			    remainingBytes -= readLength; 
			}
			
		} catch (IOException e) {
			MSGraphUtil.logAndThrowMSGraphException(LOGGER, "Error uploading file chunk", e);
		}
		
		return file;
	}
	
	private OneDriveFile uploadChunk(byte[] bytes, int readLength, long chunkRangeStart, long totalContentLength, 
			MSGraphSession msSession, UploadSession uploadSession) throws MSGraphException {
		
		OneDriveFile file = null;
		String contentRange = "bytes " + chunkRangeStart + "-" + 
				(chunkRangeStart + readLength - 1) + "/" + totalContentLength;
		
		MultiValueMap<String, String> headers = msSession.commonHeaders();
		headers.set("Content-Range", contentRange);
		headers.set("Content-Length", String.valueOf(readLength));
		
		try {
			
			LOGGER.debug("Upload chunk: {}, of size: {}", contentRange, totalContentLength);
			
			RequestEntity<byte[]> requestEntity = new RequestEntity<byte[]>(
					bytes, headers, HttpMethod.PUT, new URI(uploadSession.getUploadUrl()));
			
			ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);
			
			if (responseEntity.getStatusCode() != HttpStatus.OK && 
					responseEntity.getStatusCode() != HttpStatus.CREATED &&
					responseEntity.getStatusCode() != HttpStatus.ACCEPTED) {
				throw new MSGraphException("Unable to upload file chunk");
			}
			
			if (responseEntity.getStatusCode() == HttpStatus.OK ||
					responseEntity.getStatusCode() == HttpStatus.CREATED) {
				file = JsonUtil.jsonToJavaPojo(responseEntity.getBody(), OneDriveFile.class);
			}
			
		} catch (Exception e) {
			MSGraphUtil.logAndThrowMSGraphException(LOGGER, "Error uploading file chunk", e);
		}
		
		return file;
	}

	public static FileUploadHandler create(RestTemplate restTemplate) {
		FileUploadHandler handler = new FileUploadHandler();
		handler.restTemplate = restTemplate;
		return handler;
	}
}
