package com.enablix.commons.dms.disk;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import com.enablix.commons.dms.api.Document;
import com.enablix.commons.dms.api.DocumentMetadata;
import com.enablix.commons.dms.api.DocumentStore;
import com.enablix.commons.util.StringUtil;

@Service
public class DiskDocumentStore implements DocumentStore<DiskDocumentMetadata, DiskDocument> {

	private static final Logger LOGGER = LoggerFactory.getLogger(DiskDocumentStore.class);
	
	@Autowired
	private DiskStoreLocationResolver locationResolver;
	
	@Autowired
	private ArchiveDocumentService archiveService;
	
	@Override
	public DiskDocumentMetadata save(DiskDocument document) throws IOException {
		
		DiskDocumentMetadata docMetadata = document.getMetadata();
		
		if (StringUtil.isEmpty(docMetadata.getLocation())) {
			docMetadata.setLocation(locationResolver.getDocumentStoragePath(docMetadata));
		}
		
		LOGGER.debug("Saving document {} to: {}", docMetadata.getName(), docMetadata.getLocation());
		saveToStorage(document);
		
		return document.getMetadata();
	}

	private void saveToStorage(DiskDocument doc) throws IOException {
		
		DiskDocumentMetadata docMetadata = doc.getMetadata();
		
		File file = new File(docMetadata.getLocation());
		
		if (file.exists()) {
			archiveService.archiveDocument(docMetadata);
		}
		
		FileOutputStream fos = null;
		
		try {
			
			fos = new FileOutputStream(file);
			FileCopyUtils.copy(doc.getDataStream(), fos);
			
		} finally {
			
			if (fos != null) {
				try { fos.close(); } catch (Exception e) {
					
				}
			}
		}
	}
	
	@Override
	public DiskDocument load(DiskDocumentMetadata docMetadata) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean canHandle(DocumentMetadata docMetadata) {
		return docMetadata instanceof DiskDocumentMetadata;
	}

	@Override
	public boolean canHandle(Document<?> doc) {
		return doc instanceof DiskDocument;
	}

}
