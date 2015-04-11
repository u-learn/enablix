package com.enablix.commons.dms.disk.impl;

import java.io.File;
import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.enablix.commons.dms.disk.ArchiveDocumentService;
import com.enablix.commons.dms.disk.DiskDocumentMetadata;


/**
 * Archives the document by renaming the document with timestamp added as a 
 * suffix to the document name.
 * 
 * @author Dikshit.Luthra
 *
 */
@Component
public class ArchiveDocumentServiceImpl implements ArchiveDocumentService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ArchiveDocumentServiceImpl.class);
	
	@Override
	public String archiveDocument(DiskDocumentMetadata docMetadata) {
		
		File file = new File(docMetadata.getLocation());
		
		if (file.exists()) {
			
			File destFile = getArchiveFile(docMetadata);
			
			LOGGER.debug("{} already exists. Archiving it to: {}", 
					file.getAbsoluteFile(), destFile.getAbsolutePath());
			
			file.renameTo(destFile);
		}
		
		return file.getAbsolutePath();
	}
	
	private File getArchiveFile(DiskDocumentMetadata dm) {
		
		long currentTime = Calendar.getInstance().getTimeInMillis();
		File archiveFile = new File(dm.getLocation() + "." + currentTime);
		
		if (archiveFile.exists()) {
			archiveFile.delete();
		}
		
		return archiveFile;
	}

	 
	
}
