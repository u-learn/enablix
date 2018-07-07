package com.enablix.dms.onedrive;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.commons.dms.api.AbstractDocumentStore;
import com.enablix.commons.dms.api.BasicDocument;
import com.enablix.commons.dms.api.Document;
import com.enablix.commons.dms.api.DocumentBuilder;
import com.enablix.commons.dms.api.DocumentMetadata;
import com.enablix.core.api.DocInfo;
import com.enablix.core.api.IDocument;
import com.enablix.core.domain.config.Configuration;
import com.enablix.ms.graph.MSGraphException;
import com.enablix.ms.graph.MSGraphSDK;
import com.enablix.ms.graph.model.OneDriveFile;
import com.enablix.ms.graph.model.ParentReference;

@Component
public class OneDriveDocumentStore extends AbstractDocumentStore<OneDriveDocumentMetadata, OneDriveDocument> {

	private static final Logger LOGGER = LoggerFactory.getLogger(OneDriveDocumentStore.class);

	@Autowired
	private OneDriveDocumentBuilder docBuilder;
	
	@Autowired
	private MSGraphSDK msGraphSDK;
	
	@Autowired
	private AuthenticatedActionExecutor actionExecutor;

	@Override
	public OneDriveDocumentMetadata save(OneDriveDocument document, String contentPath) throws IOException {
		saveAndUpdateDocInfo(document, contentPath);
		return document.getMetadata();
	}
	
	protected void saveAndUpdateDocInfo(OneDriveDocument doc, String contentPath) throws IOException {
		
		Configuration config = getDocStoreConfiguration();
		
		actionExecutor.loginAndExecute(config, (msSession) -> {
			
			String baseFolder = OneDriveUtil.getBaseFolder(config);
			
			String filePath = createFilepath(baseFolder, contentPath);
			OneDriveDocumentMetadata docInfo = doc.getDocInfo();
			String filename = docInfo.getName();
		    String fileLocation = filePath + "/" + filename;
		    
			try {
			
				OneDriveFile oneDriveFile = msGraphSDK.uploadFile(
						msSession, filePath, filename, doc.getDataStream(), doc.getContentLength());
				
				docInfo.setLocation(
			    		OneDriveUtil.getFileLocationRelativeToBaseFolder(fileLocation, baseFolder));
				
				setOneDriveIds(docInfo, oneDriveFile);
				
			} catch (MSGraphException e) {
				LOGGER.error("Failed to upload file on one drive", e);
				throw new IOException("Failed to uploade file on one drive", e);
			}
			
			return null;
		});
	    
	}
	
	private void setOneDriveIds(OneDriveDocumentMetadata docMetadata, OneDriveFile file) {
		
		docMetadata.setDriveFileId(file.getId());
		
		ParentReference parentReference = file.getParentReference();
		if (parentReference != null) {
			docMetadata.setDriveParentId(parentReference.getId());
		}
		
	}

	@Override
	public OneDriveDocument load(OneDriveDocumentMetadata docMetadata) throws IOException {
		return load(docMetadata, (is) -> new OneDriveDocument(is, docMetadata));
	}
	
	public <R> R load(OneDriveDocumentMetadata docMetadata, Function<InputStream, R> func) throws IOException {
		
		Configuration config = getDocStoreConfiguration();
		
		return actionExecutor.loginAndExecute(getDocStoreConfiguration(), (msSession) -> {
			
			R document = null;
			
			try {
				
				String driveFileId = docMetadata.getDriveFileId();
				
				InputStream fileStream = driveFileId != null ?
						msGraphSDK.getFileStreamById(msSession, driveFileId) :
						msGraphSDK.getFileStreamByPath(msSession, 
							OneDriveUtil.createFileLocationWithBaseFolder(
									docMetadata.getLocation(), OneDriveUtil.getBaseFolder(config)));
			    
			    document = func.apply(fileStream);
			    
			} catch(MSGraphException ex){

				LOGGER.error("Error loading document from One Drive server", ex);
				throw new IOException(ex.getMessage(), ex);
			}

			return document;

		});
	}
	
	@Override
	public OneDriveDocumentMetadata move(OneDriveDocumentMetadata docMetadata, String newContentPath) throws IOException {

		Configuration config = getDocStoreConfiguration();
		
		return actionExecutor.loginAndExecute(config, (msSession) -> {
			
			String baseFolder = OneDriveUtil.getBaseFolder(config);
			String moveToFilepath = createFilepath(baseFolder, newContentPath);
			String filename = docMetadata.getName();
			String moveToFileLocation = createFileLocation(moveToFilepath, filename);
			
			try {
				
				msGraphSDK.moveFile(msSession, docMetadata.getDriveFileId(), moveToFilepath, filename);
				docMetadata.setFileLocation(
						OneDriveUtil.getFileLocationRelativeToBaseFolder(moveToFileLocation, baseFolder));
				
			} catch (MSGraphException e) {
				
				String msg = "Unable to move file [" + filename + "] to [" + moveToFileLocation + "]";
				LOGGER.error(msg, e);
				throw new IOException(msg, e);
			}
			    
			return docMetadata;
		});
	}

	private String createFileLocation(String filepath, String filename) {
		return filepath + "/" + filename;
	}

	@Override
	public void delete(OneDriveDocumentMetadata docMetadata) throws IOException {
		
		Configuration config = getDocStoreConfiguration();
		
		actionExecutor.loginAndExecute(config, (msSession) -> {

			try {
				
				msGraphSDK.deleteFile(msSession, docMetadata.getDriveFileId());
				
			} catch(MSGraphException ex){
				
				String msg = "Error deleting file [" + docMetadata.getFileLocation() + "] from One Drive";
				LOGGER.error(msg, ex);
				throw new IOException(msg, ex);
				
			}
			
			return null;
		});

	}

	private String createFilepath(String baseFolder, String contentPath) {
		return OneDriveUtil.createFileLocationWithBaseFolder(contentPath, baseFolder);
	}
	
	@Override
	public boolean canHandle(DocumentMetadata docMetadata) {
		return docMetadata instanceof OneDriveDocumentMetadata;
	}

	@Override
	public boolean canHandle(Document<?> doc) {
		return doc instanceof OneDriveDocument;
	}

	@Override
	public String type() {
		return "ONEDRIVE";
	}

	@Override
	public DocumentBuilder<OneDriveDocumentMetadata, OneDriveDocument> getDocumentBuilder() {
		return docBuilder;
	}

	@Override
	public DocInfo save(IDocument document, String path) throws IOException {
		
		OneDriveDocument oneDriveDoc = new OneDriveDocument(document.getDataStream(), 
				createOneDriveDocMetadata(document.getDocInfo()));
		
		saveAndUpdateDocInfo(oneDriveDoc, path);
		
		return oneDriveDoc.getDocInfo();
	}

	@Override
	public IDocument load(DocInfo docInfo) throws IOException {
		return load(createOneDriveDocMetadata(docInfo), (is) -> new BasicDocument(docInfo, is));
	}
	
	private OneDriveDocumentMetadata createOneDriveDocMetadata(DocInfo docInfo) {
		
		OneDriveDocumentMetadata oneDriveBean = 
				new OneDriveDocumentMetadata(docInfo.getName(), docInfo.getContentType());
		
		BeanUtils.copyProperties(docInfo, oneDriveBean);
		
		return oneDriveBean;
	}

}
