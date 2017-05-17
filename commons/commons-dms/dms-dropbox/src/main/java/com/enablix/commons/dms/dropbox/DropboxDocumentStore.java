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
import com.enablix.commons.dms.api.BasicDocument;
import com.enablix.commons.dms.api.Document;
import com.enablix.commons.dms.api.DocumentBuilder;
import com.enablix.commons.dms.api.DocumentMetadata;
import com.enablix.commons.util.StringUtil;
import com.enablix.core.api.DocInfo;
import com.enablix.core.api.IDocument;
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

        saveAndUpdateDocInfo(document, contentPath);
        
		return document.getMetadata();
	}
	
	protected void saveAndUpdateDocInfo(IDocument document, String contentPath) throws IOException {
		
		DbxClient client = createDbxClient();
        
        try {
			
        	LOGGER.debug("Linked account: {}", client.getAccountInfo().displayName);
			
        	InputStream inputStream = document.getDataStream();
			
	        try {
	        	
	        	String fileLocation = createDropboxFilepath(document.getDocInfo(), contentPath);
	        	
				DbxEntry.File uploadedFile = client.uploadFile(
	        			fileLocation, determineWriteMode(client, fileLocation), 
	        			document.getDocInfo().getContentLength(), inputStream);
	        	
				fileLocation = uploadedFile.path;
				document.getDocInfo().setLocation(fileLocation);
				
	        	LOGGER.debug("Uploaded: {}", uploadedFile.toString());
	        	
	        } finally {
	            inputStream.close();
	        }
	        
		} catch (DbxException e) {
			LOGGER.error("Error connecting to Dropbox", e);
			throw new IOException(e);
		}
        
	}
	
	private DbxWriteMode determineWriteMode(DbxClient client, String docLocation) throws DbxException {
		
		DbxWriteMode mode = DbxWriteMode.add();
		
		if (!StringUtil.isEmpty(docLocation)) {
			
			DbxEntry entry = client.getMetadata(docLocation);
			
			if (entry != null && entry instanceof DbxEntry.File) {
				DbxEntry.File file = (DbxEntry.File) entry;
				mode = DbxWriteMode.update(file.rev);
			}
			
		}
		
		return mode;
	}

	private String createDropboxFilepath(DocInfo md, String contentPath) {
		return "/" + contentPath + "/" + md.getName();
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
		return new DropboxDocument(getDocReader(docMetadata.getLocation()), docMetadata);
	}
	
	private DropboxReader getDocReader(String docLocation) throws IOException {
		
		DbxClient client = createDbxClient();
		
		DropboxReader reader = null;
		
        try {
        	
        	Downloader downloader = client.startGetFile(docLocation, null);
			reader = new DropboxReader(downloader);
			
            LOGGER.debug("Metadata: {}", downloader.metadata);
            
        } catch (DbxException e) {
			LOGGER.error("Error loading file from dropbox", e);
			throw new IOException(e);
			
		} 
        
        return reader;
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

	@Override
	public DropboxDocumentMetadata move(DropboxDocumentMetadata docMetadata, 
			String newContentPath) throws IOException {
		
		DbxClient client = createDbxClient();
        
        try {
			
        	LOGGER.debug("Linked account: {}", client.getAccountInfo().displayName);
        	
        	String newFileLoc = createDropboxFilepath(docMetadata, newContentPath);
        	
        	String oldLoc = docMetadata.getLocation();
        	
        	LOGGER.debug("Moving file from: {}, to: {}", oldLoc, newFileLoc);
			DbxEntry dbxFileEntry = client.move(oldLoc, newFileLoc);
        	
			if (dbxFileEntry == null) {
				// move command may have failed because of existing file with same name
				// try DELETE and MOVE approach
				dbxFileEntry = tryDeleteAndMove(client, oldLoc, newFileLoc);
			}
			
			if (dbxFileEntry == null) {
				LOGGER.error("Error moving file from: {}, to: {}", oldLoc, newFileLoc);
				throw new IOException("Unable to move file");
			}
			
			docMetadata.setLocation(dbxFileEntry.path);
			
        	LOGGER.debug("File move from: {}, to: {}", oldLoc, dbxFileEntry.path);
	        	
	        
		} catch (DbxException e) {
			LOGGER.error("Error connecting to Dropbox", e);
			throw new IOException(e);
		}
        
		return docMetadata;
	}

	private DbxEntry tryDeleteAndMove(DbxClient client, String oldLoc, String newFileLoc) throws DbxException, IOException {
		
		LOGGER.debug("Trying delete and move as a file already exists in the new location: {}", newFileLoc);
		
		DbxEntry newLocFile = null;
		
		String archiveFileLoc = newFileLoc + "." + System.currentTimeMillis();
		DbxEntry archivedFile = client.move(newFileLoc, archiveFileLoc);
		
		if (archivedFile != null) {
			
			newLocFile = client.move(oldLoc, newFileLoc);
			client.delete(archiveFileLoc);
			
		} else {
			throw new IOException("Unable to archive existing file: " + newFileLoc);
		}
		
		return newLocFile;
	}
	
	/*private DbxEntry tryDownloadAndUpload(DbxClient client, String oldLoc, 
			String newFileLoc, DropboxDocumentMetadata docMetadata) throws DbxException, IOException {
		
		LOGGER.debug("Trying upload approach as a file already exists in the new location: {}", newFileLoc);
		
		DropboxReader docReader = getDocReader(oldLoc);
		
		DbxEntry.File uploadedFile = client.uploadFile(
				newFileLoc, determineWriteMode(client, newFileLoc), 
				docMetadata.getContentLength(), docReader);

		// delete the file from temporary location
		client.delete(oldLoc);
			
		return uploadedFile;
	}*/

	@Override
	public void delete(DropboxDocumentMetadata docMetadata) throws IOException {
		
		DbxClient client = createDbxClient();
        
        try {
			
        	LOGGER.debug("Linked account: {}", client.getAccountInfo().displayName);
        	
        	client.delete(docMetadata.getLocation());
        	
        	LOGGER.debug("File [{}] deleted from dropbox", docMetadata.getLocation());
	        
		} catch (DbxException e) {
			LOGGER.error("Error connecting to Dropbox", e);
			throw new IOException(e);
		}
        
	}

	@Override
	public DocInfo save(IDocument document, String path) throws IOException {
		saveAndUpdateDocInfo(document, path);
		return document.getDocInfo();
	}

	@Override
	public BasicDocument load(DocInfo docInfo) throws IOException {
		return new BasicDocument(docInfo, getDocReader(docInfo.getLocation()));
	}

}
