package com.enablix.ms.graph;

import java.io.InputStream;

import com.enablix.ms.graph.model.OneDriveFile;
import com.enablix.ms.graph.model.OneDriveFolder;

public interface MSGraphSDK {

	MSGraphSession loginAsApp(String clientId, String clientSecret, String orgId, String driveOwnerId) throws MSGraphException;

	OneDriveFile uploadFile(MSGraphSession msSession, String filePath, String filename,
			InputStream dataStream, long contentLength) throws MSGraphException;

	InputStream getFileStreamById(MSGraphSession msSession, String driveItemId) throws MSGraphException;
	
	InputStream getFileStreamByPath(MSGraphSession msSession, String fileLocation) throws MSGraphException;

	OneDriveFolder moveFile(MSGraphSession spSession, String driveItemId, String moveToFilepath, String filename) throws MSGraphException;

	void deleteFile(MSGraphSession msSession, String driveItemId) throws MSGraphException;

}
