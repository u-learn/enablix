package com.enablix.commons.dms.dropbox;

import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.DbxDownloader;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.GetMetadataErrorException;
import com.dropbox.core.v2.files.LookupError;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.files.WriteMode;
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
		
		DbxClientV2 client = createDbxClient();
        
        try {
			
        	logLinkedAccountName(client);
			
        	InputStream inputStream = document.getDataStream();
			
	        try {
	        	
	        	String fileLocation = createDropboxFilepath(document.getDocInfo(), contentPath);
	        	
				FileMetadata uploadedFile = 
						client.files().uploadBuilder(fileLocation)
									  .withMode(determineWriteMode(client, fileLocation))
									  .uploadAndFinish(inputStream);
	        	
				fileLocation = uploadedFile.getPathLower();
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
	
	private WriteMode determineWriteMode(DbxClientV2 client, String docLocation) throws DbxException {
		
		WriteMode mode = WriteMode.ADD;
		
		if (!StringUtil.isEmpty(docLocation)) {
			
			Metadata entry = null;
			
			try {
				
				entry = client.files().getMetadata(docLocation);
				
			} catch (GetMetadataErrorException e) {
				
				if (e.errorValue.getPathValue() == LookupError.NOT_FOUND) {
					//ignore, if not found, then the write mode is to ADD
				} else {
					throw e;
				}
			}
			
			
			if (entry != null && entry instanceof FileMetadata) {
				FileMetadata file = (FileMetadata) entry;
				mode = WriteMode.update(file.getRev());
			}
			
		}
		
		return mode;
	}

	private String createDropboxFilepath(DocInfo md, String contentPath) {
		return (contentPath.charAt(0) == '/' ? "" : "/") + contentPath + "/" + md.getName();
	}

	private DbxClientV2 createDbxClient() {
		
		Configuration configuration = getDocStoreConfiguration();
		
		DbxRequestConfig config = DbxRequestConfig.newBuilder(getAppName(configuration)).build();
        
        DbxClientV2 client = new DbxClientV2(config, getAccessToken(configuration));
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
	
	private InputStream getDocReader(String docLocation) throws IOException {
		
		DbxClientV2 client = createDbxClient();
		
		InputStream reader = null;
		
        try {
        	
        	DbxDownloader<FileMetadata> downloader = client.files().download(docLocation);
			reader = downloader.getInputStream();
			
            LOGGER.debug("Metadata: {}", downloader.getResult());
            
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
	

	@Override
	public DropboxDocumentMetadata move(DropboxDocumentMetadata docMetadata, 
			String newContentPath) throws IOException {
		
		DbxClientV2 client = createDbxClient();
        
        try {
			
        	logLinkedAccountName(client);
        	
        	String newFileLoc = createDropboxFilepath(docMetadata, newContentPath);
        	
        	String oldLoc = docMetadata.getLocation();
        	
        	LOGGER.debug("Moving file from: {}, to: {}", oldLoc, newFileLoc);
			Metadata dbxFileEntry = client.files().move(oldLoc, newFileLoc);
        	
			if (dbxFileEntry == null) {
				// move command may have failed because of existing file with same name
				// try DELETE and MOVE approach
				dbxFileEntry = tryDeleteAndMove(client, oldLoc, newFileLoc);
			}
			
			if (dbxFileEntry == null) {
				LOGGER.error("Error moving file from: {}, to: {}", oldLoc, newFileLoc);
				throw new IOException("Unable to move file");
			}
			
			docMetadata.setLocation(dbxFileEntry.getPathLower());
			
        	LOGGER.debug("File move from: {}, to: {}", oldLoc, dbxFileEntry.getPathLower());
	        	
	        
		} catch (DbxException e) {
			LOGGER.error("Error connecting to Dropbox", e);
			throw new IOException(e);
		}
        
		return docMetadata;
	}

	private void logLinkedAccountName(DbxClientV2 client) throws DbxApiException, DbxException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Linked account: {}", client.users().getCurrentAccount().getName().getDisplayName());
		}
	}

	private Metadata tryDeleteAndMove(DbxClientV2 client, String oldLoc, String newFileLoc) throws DbxException, IOException {
		
		LOGGER.debug("Trying delete and move as a file already exists in the new location: {}", newFileLoc);
		
		Metadata newLocFile = null;
		
		String archiveFileLoc = newFileLoc + "." + System.currentTimeMillis();
		Metadata archivedFile = client.files().move(newFileLoc, archiveFileLoc);
		
		if (archivedFile != null) {
			
			newLocFile = client.files().move(oldLoc, newFileLoc);
			client.files().delete(archiveFileLoc);
			
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
		
		DbxClientV2 client = createDbxClient();
        
        try {
			
        	logLinkedAccountName(client);
        	
        	client.files().delete(docMetadata.getLocation());
        	
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
