package com.enablix.commons.dms.disk;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import com.enablix.commons.dms.api.BasicDocInfo;
import com.enablix.commons.dms.api.BasicDocument;
import com.enablix.commons.dms.api.Document;
import com.enablix.commons.dms.api.DocumentBuilder;
import com.enablix.commons.dms.api.DocumentMetadata;
import com.enablix.commons.dms.api.DocumentStore;
import com.enablix.core.api.DocInfo;
import com.enablix.core.api.IDocument;

@Service
public class DiskDocumentStore implements DocumentStore<DiskDocumentMetadata, DiskDocument> {

	private static final Logger LOGGER = LoggerFactory.getLogger(DiskDocumentStore.class);
	
	@Autowired
	private DiskStoreLocationResolver locationResolver;
	
	@Autowired
	private ArchiveDocumentService archiveService;
	
	@Autowired
	private DiskDocumentBuilder docBuilder;
	
	@Override
	public DiskDocumentMetadata save(DiskDocument document, String contentPath) throws IOException {
		saveDocument(document, contentPath);
		return document.getMetadata();
	}

	protected void saveDocument(IDocument doc, String contentPath) throws IOException {

		DocInfo docMetadata = doc.getDocInfo();
		docMetadata.setLocation(locationResolver.getDocumentStoragePath(docMetadata, contentPath));
		
		LOGGER.debug("Saving document {} to: {}", docMetadata.getName(), docMetadata.getLocation());
		saveToStorage(doc);
	}
	
	private void saveToStorage(IDocument doc) throws IOException {
		
		DocInfo docMetadata = doc.getDocInfo();
		
		File file = new File(docMetadata.getLocation());
		
		if (file.exists()) {
			archiveService.archiveDocument(docMetadata);
		}
		
		FileOutputStream fos = null;
		
		try {
			
			fos = new FileOutputStream(file);
			FileCopyUtils.copy(doc.getDataStream(), fos);
			docMetadata.setContentLength(file.length());
			
		} finally {
			
			if (fos != null) {
				try { fos.close(); } catch (Exception e) {
					
				}
			}
		}
	}
	
	@Override
	public DiskDocument load(DiskDocumentMetadata docMetadata) {
		return new DiskDocument(docMetadata);
	}

	@Override
	public boolean canHandle(DocumentMetadata docMetadata) {
		return docMetadata instanceof DiskDocumentMetadata;
	}

	@Override
	public boolean canHandle(Document<?> doc) {
		return doc instanceof DiskDocument;
	}

	@Override
	public String type() {
		return "DISK";
	}

	@Override
	public DocumentBuilder<DiskDocumentMetadata, DiskDocument> getDocumentBuilder() {
		return docBuilder;
	}

	@Override
	public DiskDocumentMetadata move(DiskDocumentMetadata docMetadata, String newContentPath) throws IOException {
		
		String existLoc = docMetadata.getLocation();
		String newLoc = locationResolver.getDocumentStoragePath(docMetadata, newContentPath);
		
		File existFile = new File(existLoc);
		if (!existFile.exists()) {
			throw new FileNotFoundException("File [" + existLoc + "] does not exist");
		}
		
		// archive if the file already exist
		File newFile = new File(newLoc);
		
		File parentFolder = newFile.getParentFile();
		if (!parentFolder.exists()) {
			parentFolder.mkdirs();
		}
		
		if (newFile.exists()) {
			
			BasicDocInfo newFileDummyInfo = new BasicDocInfo();
			newFileDummyInfo.setContentLength(docMetadata.getContentLength());
			newFileDummyInfo.setContentType(docMetadata.getContentType());
			newFileDummyInfo.setLocation(newLoc);
			newFileDummyInfo.setName(docMetadata.getName());
			
			archiveService.archiveDocument(newFileDummyInfo);
		}
		
		// move the file
		Files.move(FileSystems.getDefault().getPath(existLoc), 
				   FileSystems.getDefault().getPath(newLoc), 
				   StandardCopyOption.ATOMIC_MOVE);
		
		// update document metadata with new location
		docMetadata.setLocation(newLoc);
		
		return docMetadata;
	}

	@Override
	public void delete(DiskDocumentMetadata docMetadata) throws IOException {
		archiveService.archiveDocument(docMetadata);
	}

	@Override
	public DocInfo save(IDocument document, String path) throws IOException {
		saveDocument(document, path);
		return document.getDocInfo();
	}

	@Override
	public BasicDocument load(DocInfo docInfo) throws IOException {
		return new BasicDocument(docInfo, FileStreamHelper.getFileInputStream(docInfo));
	}

}
