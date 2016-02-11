package com.enablix.commons.dms.dropbox;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxClient.Downloader;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWriteMode;
import com.enablix.commons.dms.api.AbstractDocumentStore;
import com.enablix.commons.dms.api.Document;
import com.enablix.commons.dms.api.DocumentBuilder;
import com.enablix.commons.dms.api.DocumentMetadata;
import com.enablix.commons.util.StringUtil;
import com.enablix.core.domain.config.Configuration;

@Component
public class DropboxDocumentStore extends AbstractDocumentStore<DropboxDocumentMetadata, DropboxDocument> {

	private static final String APP_NAME_KEY = "APP_NAME";

	private static final String ACCESS_TOKEN_KEY = "ACCESS_TOKEN";

	private static final Logger LOGGER = LoggerFactory.getLogger(DropboxDocumentStore.class);
	
	// Get your app key and secret from the Dropbox developers website.
    final String APP_KEY = "wuf46xrsqcaqc3f";
    final String APP_SECRET = "yguzlxgb5lz0a0b";
    final String ACCESS_TOKEN = "jKRRnQ3qhUkAAAAAAAAJbGneMwCiSJHkgvyKy5Vn3xQ5MfqYY0DBfCDpEmXlMzLM";
    final String APP_NAME = "Enablix Local";

    @Autowired
	private DropboxDocumentBuilder docBuilder;
	
	@Override
	public DropboxDocumentMetadata save(DropboxDocument document, String contentPath) throws IOException {

        DbxClient client = createDbxClient();
        
        try {
			
        	LOGGER.debug("Linked account: {}", client.getAccountInfo().displayName);
			
        	InputStream inputStream = document.getDataStream();
			
	        try {
	        	
	        	String fileLocation = createDropboxFilepath(document, contentPath);
	        	
				DbxEntry.File uploadedFile = client.uploadFile(
	        			fileLocation, determineWriteMode(client, document), 
	        			document.getContentLength(), inputStream);
	        	
				fileLocation = uploadedFile.path;
				document.getMetadata().setLocation(fileLocation);
				
	        	LOGGER.debug("Uploaded: {}", uploadedFile.toString());
	        	
	        } finally {
	            inputStream.close();
	        }
	        
		} catch (DbxException e) {
			LOGGER.error("Error connecting to Dropbox", e);
			throw new IOException(e);
		}
        
		return document.getMetadata();
	}
	
	private DbxWriteMode determineWriteMode(DbxClient client, DropboxDocument doc) throws DbxException {
		
		DbxWriteMode mode = DbxWriteMode.add();
		
		String location = doc.getMetadata().getLocation();
		
		if (!StringUtil.isEmpty(location)) {
			
			DbxEntry entry = client.getMetadata(location);
			
			if (entry != null && entry instanceof DbxEntry.File) {
				DbxEntry.File file = (DbxEntry.File) entry;
				mode = DbxWriteMode.update(file.rev);
			}
			
		}
		
		return mode;
	}

	private String createDropboxFilepath(DropboxDocument document, String contentPath) {
		return "/" + contentPath + "/" + document.getMetadata().getName();
	}

	private DbxClient createDbxClient() {
		
		Configuration configuration = getDocStoreConfiguration();
		
		DbxRequestConfig config = new DbxRequestConfig(
				getAppName(configuration), Locale.getDefault().toString());
        
        DbxClient client = new DbxClient(config, getAccessToken(configuration));
		return client;
	}
	
	private String getAppName(Configuration config) {
		return config.getStringValue(APP_NAME_KEY);
	}
	
	private String getAccessToken(Configuration config) {
		return config.getStringValue(ACCESS_TOKEN_KEY);
	}

	@Override
	public DropboxDocument load(DropboxDocumentMetadata docMetadata) throws IOException {
		
		DbxClient client = createDbxClient();
		
		DropboxReader reader = null;
		
        try {
        	
        	Downloader downloader = client.startGetFile(docMetadata.getLocation(), null);
			reader = new DropboxReader(downloader);
			
            LOGGER.debug("Metadata: {}", downloader.metadata);
            
        } catch (DbxException e) {
			LOGGER.error("Error loading file from dropbox", e);
			throw new IOException(e);
			
		} 
        
		return new DropboxDocument(reader, docMetadata);
	}

	@Override
	public boolean canHandle(DocumentMetadata docMetadata) {
		return docMetadata instanceof DropboxDocumentMetadata;
	}

	@Override
	public boolean canHandle(Document<?> doc) {
		return doc instanceof DropboxDocument;
	}

	@Override
	public String type() {
		return "DROPBOX";
	}
	
	@Override
	public DocumentBuilder<DropboxDocumentMetadata, DropboxDocument> getDocumentBuilder() {
		return docBuilder ;
	}
	
	private static class DropboxReader extends InputStream {

		private Downloader downloader;
		
		private DropboxReader(Downloader downloader) {
			this.downloader = downloader;
		}
		
		@Override
		public int read() throws IOException {
			return downloader.body.read();
		}
		
	}

}
