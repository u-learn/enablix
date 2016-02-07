package com.enablix.commons.dms.webdav;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.jackrabbit.webdav.DavConstants;
import org.apache.jackrabbit.webdav.DavException;
import org.apache.jackrabbit.webdav.client.methods.DavMethod;
import org.apache.jackrabbit.webdav.client.methods.MkColMethod;
import org.apache.jackrabbit.webdav.client.methods.PropFindMethod;
import org.apache.jackrabbit.webdav.client.methods.PutMethod;
import org.apache.jackrabbit.webdav.property.DavPropertyName;
import org.apache.jackrabbit.webdav.property.DavPropertyNameSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.commons.dms.api.AbstractDocumentStore;
import com.enablix.commons.dms.api.Document;
import com.enablix.commons.dms.api.DocumentBuilder;
import com.enablix.commons.dms.api.DocumentMetadata;
import com.enablix.commons.util.StringUtil;
import com.enablix.core.domain.config.Configuration;

@Component
public class WebDAVDocumentStore extends AbstractDocumentStore<WebDAVDocumentMetadata, WebDAVDocument> {

	private static final Logger LOGGER = LoggerFactory.getLogger(WebDAVDocumentStore.class);
	
	private static final String BASE_DOC_PATH_KEY = "BASE_DOC_PATH";

	private static final String USERNAME_KEY = "USERNAME";

	private static final String PASSWORD_KEY = "PASSWORD";
	
	private static final String HOST_KEY = "HOST";
	
	private static final String MAX_HOST_CONNECTIONS_KEY = "MAX_HOST_CONNECTIONS";

	private static final String CONN_PARAMS_PREFIX = "conn.params.";

	@Autowired
	private WebDAVDocumentBuilder docBuilder;

	@Override
	public WebDAVDocumentMetadata save(WebDAVDocument document, String contentPath) throws IOException {
		
		try {
			
			Configuration config = getDocStoreConfiguration();
			
		    HttpClient client = createHttpClient(config);

			String baseWebDAVPath = config.getStringValue(BASE_DOC_PATH_KEY);

			String host = config.getStringValue(HOST_KEY);
		    String fileLocation = createFilepath(baseWebDAVPath, document, contentPath);
			
			createFolder(host, baseWebDAVPath, contentPath, client);
			
		    PutMethod method = new PutMethod(getResourceURI(host, fileLocation));
		    RequestEntity requestEntity = new InputStreamRequestEntity(document.getDataStream());
		    method.setRequestEntity(requestEntity);
		    client.executeMethod(method);
		    
		    document.getMetadata().setFileLocation(fileLocation);
		    
		    LOGGER.debug(method.getStatusCode() + " " + method.getStatusText());
		    
		    checkMethodSuccess(method, "Unable to upload file");
		    
		} catch(HttpException ex){
			LOGGER.error("Error saving document on webDAV server", ex);
			throw ex;
			
		} catch(IOException ex){
		    LOGGER.error("Error saving document on webDAV server", ex);
		    throw ex;
		    
		}
		
		return document.getMetadata();
	}

	private String getResourceURI(String host, String fileLocation) {
		return sanitizeURI(host + fileLocation);
	}

	private HttpClient createHttpClient(Configuration config) {

		// TODO: initialization should be done once only instead of reading the properties on each call
		String userName = config.getStringValue(USERNAME_KEY);
		String password = config.getStringValue(PASSWORD_KEY);
		
		String maxConn = config.getStringValue(MAX_HOST_CONNECTIONS_KEY);
		int maxHostConnections = 10;
		
		if (!StringUtil.isEmpty(maxConn)) {
			
			try {
				maxHostConnections = Integer.parseInt(maxConn);
			} catch (NumberFormatException e) {
				LOGGER.error("Invalid max host connection value [{}]", maxConn);
				maxHostConnections = 10;
			}
		}
		
		Map<String, Object> connParams = new HashMap<>();
		
		for (Map.Entry<String, Object> configProp : config.getConfig().entrySet()) {
			String key = configProp.getKey();
			if (key.startsWith(CONN_PARAMS_PREFIX)) {
				String connParamKey = key.substring(CONN_PARAMS_PREFIX.length());
				connParams.put(connParamKey, configProp.getValue());
			}
		}
		
		HostConfiguration hostConfig = new HostConfiguration();
        
        HttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
        HttpConnectionManagerParams params = new HttpConnectionManagerParams();
        
        params.setMaxConnectionsPerHost(hostConfig, maxHostConnections);
        
        for (Map.Entry<String, Object> connParam : connParams.entrySet()) {
        	params.setParameter(connParam.getKey(), connParam.getValue());
        }
        
        connectionManager.setParams(params);  
        
        
        HttpClient client = new HttpClient(connectionManager);
        client.setHostConfiguration(hostConfig);

        Credentials creds = new UsernamePasswordCredentials(userName, password);
		client.getState().setCredentials(AuthScope.ANY, creds);
		
		return client;
	}
	
	private void createFolder(String host, String baseWebDAVPath, String contentPath, 
			HttpClient client) throws HttpException, IOException {
		
		String resourceURI = getResourceURI(host, baseWebDAVPath + "/" + contentPath);
		checkAndCreateRecursiveCollections(resourceURI, client);
		
	}
	
	private void createWebDAVCollection(String collectionURI, HttpClient client) 
			throws HttpException, IOException {
		
		MkColMethod method = new MkColMethod(collectionURI);
        client.executeMethod(method);
        
        LOGGER.debug("create folder status - [{}]: [{}]", method.getStatusCode(), method.getStatusText());

        checkMethodSuccess(method, "Unable to create folder - " + collectionURI);
        
    }
	
	
	private void checkAndCreateRecursiveCollections(String resourceURI, HttpClient client) throws IOException {
		
		DavPropertyNameSet properties = new DavPropertyNameSet();
		properties.add(DavPropertyName.RESOURCETYPE);
		
		PropFindMethod propFindMethod = new PropFindMethod(resourceURI, 
				DavConstants.PROPFIND_BY_PROPERTY, properties, DavConstants.DEPTH_0);
		
		client.executeMethod(propFindMethod);
		
		if (propFindMethod.getStatusCode() == HttpServletResponse.SC_NOT_FOUND) {
			
			LOGGER.debug("Resource [{}] does not exist, creating.", resourceURI);
			
			String parentURI = resourceURI;
			if (parentURI.endsWith("/")) {
				parentURI = parentURI.substring(0, resourceURI.length() - 1);
			}
			
			parentURI = parentURI.substring(0, resourceURI.lastIndexOf('/'));
			checkAndCreateRecursiveCollections(parentURI, client);
			
			// create collection now
			createWebDAVCollection(resourceURI, client);
		}
		
	}
	
	private void checkMethodSuccess(DavMethod method, String errorMsg) throws IOException {
		try {
			method.checkSuccess();
		} catch (DavException e) {
			LOGGER.error(errorMsg, e);
			throw new IOException(errorMsg, e);
		}
	}

	@Override
	public WebDAVDocument load(WebDAVDocumentMetadata docMetadata) throws IOException {
		
		WebDAVDocument document = null;
		
		try {
			
			Configuration config = getDocStoreConfiguration();
			
			String host = config.getStringValue(HOST_KEY);			
		    HttpClient client = createHttpClient(config);

		    String fileLocation = docMetadata.getFileLocation();
			
		    GetMethod method = new GetMethod(getResourceURI(host, fileLocation));
		    client.executeMethod(method);
		    
		    client.executeMethod(method);
		    LOGGER.debug(method.getStatusCode() + " " + method.getStatusText());
		    
		    if (method.getStatusCode() == 200) {
		    	
		    	InputStream docStream = method.getResponseBodyAsStream();
		    	document = new WebDAVDocument(docStream, docMetadata);
		    	
		    } else {
		    	throw new IOException("Unable to read file content. Status - " 
		    			+ method.getStatusCode() + " " + method.getStatusText());
		    }
		    
		} catch(HttpException ex){
			LOGGER.error("Error saving document on webDAV server", ex);
			throw ex;
			
		} catch(IOException ex){
		    LOGGER.error("Error saving document on webDAV server", ex);
		    throw ex;
		}

		return document;
	}

	private String createFilepath(String baseWebDAVPath, 
			WebDAVDocument document, String contentPath) {
		String fileLocation = baseWebDAVPath + "/" + contentPath + "/" + document.getMetadata().getName();
		return sanitizeURI(fileLocation);
	}
	
	private String sanitizeURI(String uri) {
		return uri.replace(" ", "%20");
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
