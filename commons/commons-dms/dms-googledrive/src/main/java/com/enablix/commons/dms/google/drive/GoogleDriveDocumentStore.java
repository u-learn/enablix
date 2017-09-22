package com.enablix.commons.dms.google.drive;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.function.Function;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.commons.dms.api.AbstractDocumentStore;
import com.enablix.commons.dms.api.BasicDocument;
import com.enablix.commons.dms.api.Document;
import com.enablix.commons.dms.api.DocumentBuilder;
import com.enablix.commons.dms.api.DocumentMetadata;
import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.core.api.DocInfo;
import com.enablix.core.api.IDocument;
import com.enablix.core.domain.config.Configuration;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;

@Component
public class GoogleDriveDocumentStore extends AbstractDocumentStore<GoogleDriveDocumentMetadata, GoogleDriveDocument> {

	public static final String DOC_STORE_TYPE = "GOOGLEDRIVE";

	private static final String BASE_FOLDER_KEY = "BASE_FOLDER";

	@Autowired
	private GoogleDriveDocumentBuilder docBuilder;
	
	@Autowired
	private GoogleDriveService driveService;

	@Override
	public GoogleDriveDocumentMetadata save(GoogleDriveDocument document, String contentPath) throws IOException {
		saveAndUpdateDocInfo(document, contentPath);
		return document.getMetadata();
	}
	
	protected void saveAndUpdateDocInfo(GoogleDriveDocument document, String contentPath) throws IOException {
		
		Configuration config = getDocStoreConfiguration();
		
	    Drive drive = driveService.getDrive(config);

		String baseGoogleDrivePath = config.getStringValue(BASE_FOLDER_KEY);
		String destPath = baseGoogleDrivePath + "/" + contentPath;

		GoogleDriveDocumentMetadata docInfo = document.getDocInfo();
		File uploadFile = driveService.uploadFile(drive, destPath, docInfo.getName(), 
				document.getDataStream(), docInfo.getContentType());
		
		docInfo.setLocation(destPath + "/" + docInfo.getName());
		setGoogleDriveIds(docInfo, uploadFile);
	}
	
	public String getBaseFolder() {
		Configuration config = getDocStoreConfiguration();
		return config.getStringValue(BASE_FOLDER_KEY);
	}
	

	@Override
	public GoogleDriveDocument load(GoogleDriveDocumentMetadata docMetadata) throws IOException {
		return load(docMetadata, (is) -> new GoogleDriveDocument(is, docMetadata));
	}
	
	protected <R> R load(GoogleDriveDocumentMetadata docMetadata, Function<InputStream, R> func) throws IOException {
		
		Configuration config = getDocStoreConfiguration();
		
		Drive drive = driveService.getDrive(config);
		InputStream inputStream = drive.files().get(docMetadata.getDriveFileId()).executeMediaAsInputStream();
		
		R document = func.apply(inputStream);
		
		return document;
	}

	@Override
	public boolean canHandle(DocumentMetadata docMetadata) {
		return docMetadata instanceof GoogleDriveDocumentMetadata;
	}

	@Override
	public boolean canHandle(Document<?> doc) {
		return doc instanceof GoogleDriveDocument;
	}

	@Override
	public String type() {
		return DOC_STORE_TYPE;
	}

	@Override
	public DocumentBuilder<GoogleDriveDocumentMetadata, GoogleDriveDocument> getDocumentBuilder() {
		return docBuilder;
	}

	@Override
	public GoogleDriveDocumentMetadata move(GoogleDriveDocumentMetadata docMetadata, String newContentPath) throws IOException {

		Configuration config = getDocStoreConfiguration();
		Drive drive = driveService.getDrive(config);
		
		String baseGoogleDrivePath = config.getStringValue(BASE_FOLDER_KEY);
		String destPath = baseGoogleDrivePath + "/" + newContentPath;
		
		File fileUpdate = driveService.moveFileFromParent(drive, 
				docMetadata.getDriveFileId(), docMetadata.getDriveParentId(), destPath);
		
		docMetadata.setLocation(destPath + "/" + docMetadata.getName());
		setGoogleDriveIds(docMetadata, fileUpdate);
		
		return docMetadata;
	}

	private void setGoogleDriveIds(GoogleDriveDocumentMetadata docMetadata, File file) {
		
		docMetadata.setDriveFileId(file.getId());
		
		String newParentId = null;
		
		List<String> parents = file.getParents();
		if (CollectionUtil.isNotEmpty(parents)) {
			newParentId = parents.get(0);
		}
		
		docMetadata.setDriveParentId(newParentId);
	}

	@Override
	public void delete(GoogleDriveDocumentMetadata docMetadata) throws IOException {
		
		Configuration config = getDocStoreConfiguration();
		Drive drive = driveService.getDrive(config);
		
		driveService.deleteFile(drive, docMetadata.getDriveFileId());
	}

	@Override
	public DocInfo save(IDocument document, String path) throws IOException {
		
		saveAndUpdateDocInfo(
				new GoogleDriveDocument(document.getDataStream(), createGoogleDocMetadata(document.getDocInfo())), 
				path);
		
		return document.getDocInfo();
	}

	@Override
	public BasicDocument load(DocInfo docInfo) throws IOException {
		return load(createGoogleDocMetadata(docInfo), (is) -> new BasicDocument(docInfo, is));
	}

	private GoogleDriveDocumentMetadata createGoogleDocMetadata(DocInfo docInfo) {
		
		GoogleDriveDocumentMetadata googleBean = 
				new GoogleDriveDocumentMetadata(docInfo.getName(), docInfo.getContentType());
		
		BeanUtils.copyProperties(docInfo, googleBean);
		
		return googleBean;
	}

	public void shareWithUser(String email) throws FileNotFoundException, IOException {
		
		Configuration config = getDocStoreConfiguration();
		Drive drive = driveService.getDrive(config);
		
		String baseFolder = config.getStringValue(BASE_FOLDER_KEY);
		String folderId = driveService.createOrFindFolderStructure(drive, baseFolder);
		
		driveService.shareFolderIdWithUser(drive, folderId, email);
	}
	
}
