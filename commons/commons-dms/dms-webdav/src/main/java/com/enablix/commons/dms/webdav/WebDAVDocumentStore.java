package com.enablix.commons.dms.webdav;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.httpclient.HttpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.commons.config.ConfigurationProvider;
import com.enablix.commons.dms.DocumentStoreConstants;
import com.enablix.commons.dms.api.Document;
import com.enablix.commons.dms.api.DocumentBuilder;
import com.enablix.commons.dms.api.DocumentMetadata;
import com.enablix.commons.dms.api.DocumentStore;
import com.enablix.core.domain.config.Configuration;
import com.github.sardine.Sardine;
import com.github.sardine.SardineFactory;

@Component
public class WebDAVDocumentStore implements DocumentStore<WebDAVDocumentMetadata, WebDAVDocument> {

	private static final Logger LOGGER = LoggerFactory.getLogger(WebDAVDocumentStore.class);
	
	private static final String USERNAME_KEY = "USERNAME";

	private static final String PASSWORD_KEY = "PASSWORD";
	
	private static final String WEBDAV_SERVER_URL_KEY = "WEBDAV_SERVER_URL";
	
	@Autowired
	private ConfigurationProvider configProvider;
	
	@Autowired
	private WebDAVDocumentBuilder docBuilder;

	@Override
	public WebDAVDocumentMetadata save(WebDAVDocument document, String contentPath) throws IOException {
		
		try {
			
		    Configuration config = configProvider.getConfiguration(DocumentStoreConstants.DOC_STORE_CONFIG_KEY);
			String webDAVServerUrl = config.getStringValue(WEBDAV_SERVER_URL_KEY);
			String username = config.getStringValue(USERNAME_KEY);
			String password = config.getStringValue(PASSWORD_KEY);
			
		    String fileLocation = document.getMetadata().getName();//createFilepath(document, contentPath);

		    Sardine sardine = SardineFactory.begin(username, password);
		    String httpFileUrl = webDAVServerUrl + "/" + fileLocation;
		    httpFileUrl = httpFileUrl.replace(" ", "%20");
		    
		    LOGGER.debug("Uploading file: {}", httpFileUrl);
		    
		    /*
		     * TODO: should be able to upload files directly from Input stream
		     * without use byte array 
		     * OR may figure out if we can save it to temporary file and then
		     * use FileEntity. Getting following exception while directly using
		     * input stream
		     * org.apache.http.client.NonRepeatableRequestException: 
		     * 		Cannot retry request with a non-repeatable request entity.
		     * 
		     * OR Try out Jackrabbit version without Enablix folder structure appended
		     * and replacing " " with "%20"
		     * */
		    ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
		    InputStream is = document.getDataStream();
		    
		    int reads = is.read(); 
		    while (reads != -1) { 
		    	baos.write(reads); 
		    	reads = is.read(); 
		    } 

		    sardine.put(httpFileUrl, baos.toByteArray());

		    document.getMetadata().setFileLocation(fileLocation);
		    
		} catch(HttpException ex) {
			LOGGER.error("Error saving document on webDAV server", ex);
			throw ex;
			
		} catch(IOException ex) {
		    LOGGER.error("Error saving document on webDAV server", ex);
		    throw ex;
		}
		
		return document.getMetadata();
	}

	@Override
	public WebDAVDocument load(WebDAVDocumentMetadata docMetadata) throws IOException {
		
		WebDAVDocument document = null;
		
		try {

			Configuration config = configProvider.getConfiguration(DocumentStoreConstants.DOC_STORE_CONFIG_KEY);
			String webDAVServerUrl = config.getStringValue(WEBDAV_SERVER_URL_KEY);
			String username = config.getStringValue(USERNAME_KEY);
			String password = config.getStringValue(PASSWORD_KEY);
			
		    Sardine sardine = SardineFactory.begin(username, password);
		    InputStream dataStream = sardine.get(webDAVServerUrl + docMetadata.getFileLocation());
		    document = new WebDAVDocument(dataStream, docMetadata);
		    
		} catch(HttpException ex){
			LOGGER.error("Error saving document on webDAV server", ex);
			throw ex;
			
		} catch(IOException ex){
		    LOGGER.error("Error saving document on webDAV server", ex);
		    throw ex;
		}

		return document;
	}

	private String createFilepath(WebDAVDocument document, String contentPath) {
		return "/" + contentPath + "/" + document.getMetadata().getName();
	}
	
	@Override
	public boolean canHandle(DocumentMetadata docMetadata) {
		return docMetadata instanceof WebDAVDocumentMetadata;
	}

	@Override
	public boolean canHandle(Document<?> doc) {
		return doc instanceof WebDAVDocument;
	}

	@Override
	public String type() {
		return "WEBDAV";
	}

	@Override
	public DocumentBuilder<WebDAVDocumentMetadata, WebDAVDocument> getDocumentBuilder() {
		return docBuilder;
	}

}
