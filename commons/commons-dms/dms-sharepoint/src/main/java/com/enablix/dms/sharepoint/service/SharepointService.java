package com.enablix.dms.sharepoint.service;

import java.io.InputStream;

import com.enablix.core.domain.config.Configuration;
import com.enablix.dms.sharepoint.SharepointException;

public interface SharepointService {

	SharepointSession login(Configuration config) throws SharepointException;
	
	void createFolder(SharepointSession session, String folderPath) throws SharepointException;

	String moveFile(SharepointSession spSession, 
			String currentFileLocation, String moveToFilepath, String filename) throws SharepointException;

	void deleteFile(SharepointSession spSession, String fileLocation) throws SharepointException;

	String uploadFile(SharepointSession session, String filepath, 
			String filename, InputStream dataStream) throws SharepointException;

	InputStream getFileStream(SharepointSession spSession, String fileLocation) throws SharepointException;
	
}
