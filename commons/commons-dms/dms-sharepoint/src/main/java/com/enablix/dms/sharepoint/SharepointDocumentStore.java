package com.enablix.dms.sharepoint;

import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.commons.dms.api.AbstractDocumentStore;
import com.enablix.commons.dms.api.Document;
import com.enablix.commons.dms.api.DocumentBuilder;
import com.enablix.commons.dms.api.DocumentMetadata;
import com.enablix.core.domain.config.Configuration;
import com.enablix.dms.sharepoint.service.SharepointService;
import com.enablix.dms.sharepoint.service.SharepointSession;

@Component
public class SharepointDocumentStore extends AbstractDocumentStore<SharepointDocumentMetadata, SharepointDocument> {

	private static final Logger LOGGER = LoggerFactory.getLogger(SharepointDocumentStore.class);

	@Autowired
	private SharepointDocumentBuilder docBuilder;
	
	@Autowired
	private SharepointService spService;

	@Override
	public SharepointDocumentMetadata save(SharepointDocument document, String contentPath) throws IOException {
		
		Configuration config = getDocStoreConfiguration();
		
		String baseFolder = config.getStringValue(SharepointConstants.CFG_BASE_FOLDER_KEY);
		
		SharepointSession spSession = login(config);
		
		String filePath = createFilepath(baseFolder, contentPath);
		String filename = document.getMetadata().getName();
	    String fileLocation;
	    
		try {
		
			fileLocation = spService.uploadFile(spSession, filePath, filename, document.getDataStream());
			
		} catch (SharepointException e) {
			LOGGER.error("Failed to upload file on sharepoint", e);
			throw new IOException("Failed to uploade file on sharepoint", e);
		}

	    document.getMetadata().setFileLocation(
	    		SharepointUtil.getFileLocationRelativeToBaseFolder(fileLocation, baseFolder));
		
		return document.getMetadata();
	}

	private SharepointSession login(Configuration config) throws IOException {
		
		SharepointSession spSession;
		
		try {
			
			spSession = spService.login(config);
			
		} catch (SharepointException e) {
			LOGGER.error("Unable to login", e);
			throw new IOException("Failed to login to sharepoint", e);
		}
		
		return spSession;
	}

	@Override
	public SharepointDocument load(SharepointDocumentMetadata docMetadata) throws IOException {
		
		SharepointDocument document = null;
		
		try {
			
			Configuration config = getDocStoreConfiguration();
			
			SharepointSession spSession = login(config);
			InputStream fileStream = spService.getFileStream(spSession, 
					SharepointUtil.createFileLocationWithBaseFolder(
							docMetadata.getFileLocation(), spSession.getBaseFolder()));
		    
		    document = new SharepointDocument(fileStream, docMetadata);
		    
		} catch(SharepointException ex){

			LOGGER.error("Error loading document from Sharepoint server", ex);
			throw new IOException(ex.getMessage(), ex);
		}

		return document;
	}
	
	@Override
	public SharepointDocumentMetadata move(SharepointDocumentMetadata docMetadata, String newContentPath) throws IOException {

		Configuration config = getDocStoreConfiguration();
		String baseFolder = config.getStringValue(SharepointConstants.CFG_BASE_FOLDER_KEY);
		
		SharepointSession spSession = login(config);
		
		String currentFileLocation = docMetadata.getFileLocation();
		String moveToFilepath = createFilepath(baseFolder, newContentPath);
		String moveToFileLocation = createFileLocation(moveToFilepath, docMetadata.getName());
		
		String newFileLoc;
		
		try {
			
			newFileLoc = spService.moveFile(spSession, currentFileLocation, moveToFilepath, docMetadata.getName());
			
		} catch (SharepointException e) {
			
			String msg = "Unable to move file to [" + moveToFileLocation + "]";
			LOGGER.error(msg, e);
			throw new IOException(msg, e);
		}
	    
	    docMetadata.setFileLocation(SharepointUtil.getFileLocationRelativeToBaseFolder(newFileLoc, baseFolder));
		    
		return docMetadata;
	}

	private String createFileLocation(String filepath, String filename) {
		return filepath + "/" + filename;
	}

	@Override
	public void delete(SharepointDocumentMetadata docMetadata) throws IOException {
		
		try {
			
			Configuration config = getDocStoreConfiguration();
			SharepointSession spSession = login(config);
			spService.deleteFile(spSession, SharepointUtil.createFileLocationWithBaseFolder(
											docMetadata.getFileLocation(), spSession.getBaseFolder()));
			
		} catch(SharepointException ex){
			
			String msg = "Error deleting file [" + docMetadata.getFileLocation() + "] from Sharepoint server";
			LOGGER.error(msg, ex);
			throw new IOException(msg, ex);
			
		} 

	}

	private String createFilepath(String baseFolder, String contentPath) {
		return baseFolder + "/" + contentPath;
	}
	
	@Override
	public boolean canHandle(DocumentMetadata docMetadata) {
		return docMetadata instanceof SharepointDocumentMetadata;
	}

	@Override
	public boolean canHandle(Document<?> doc) {
		return doc instanceof SharepointDocument;
	}

	@Override
	public String type() {
		return "SHAREPOINT";
	}

	@Override
	public DocumentBuilder<SharepointDocumentMetadata, SharepointDocument> getDocumentBuilder() {
		return docBuilder;
	}

	
}
