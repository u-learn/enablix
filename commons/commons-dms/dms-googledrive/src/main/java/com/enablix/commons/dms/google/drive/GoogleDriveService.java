package com.enablix.commons.dms.google.drive;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.commons.util.FileUtil;
import com.enablix.commons.util.StringUtil;
import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.commons.util.concurrent.Events;
import com.enablix.core.domain.config.Configuration;
import com.enablix.core.mail.BasicEmailVelocityInputBuilder;
import com.enablix.core.mail.entities.EmailRequest;
import com.enablix.core.mail.entities.EmailRequest.Recipient;
import com.enablix.core.mail.service.EmailRequestProcessor;
import com.enablix.core.mq.Event;
import com.enablix.core.mq.EventSubscription;
import com.enablix.core.mq.util.EventUtil;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.util.Utils;
import com.google.api.client.http.InputStreamContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.Permission;
import com.google.api.services.drive.model.PermissionList;

@Component
public class GoogleDriveService {

	private static final String MIME_TYPE_GOOGLE_APPS_FOLDER = "application/vnd.google-apps.folder";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GoogleDriveService.class);
	
	@Autowired
	private EmailRequestProcessor emailProcessor;
	
	public Drive getDrive(Configuration config) throws FileNotFoundException, IOException {
        
		Object keyFile = config.getConfig().get("AUTH_KEY_FILE_ENC");
		if (keyFile == null) {
			LOGGER.error("Google API authorization key file not found");
			throw new IllegalStateException("Google API authorization key file not found");
		}
		
		ByteArrayInputStream bis = null;
		if (keyFile instanceof Map) {
			String fileData = (String) ((Map<?, ?>) keyFile).get("data");
			bis = new ByteArrayInputStream(fileData.getBytes());
		}
		
		if (bis == null) {
			LOGGER.error("Google API authorization key file data not found");
			throw new IllegalStateException("Google API authorization key file data not found");
		}
		
		GoogleCredential credential = 
        		GoogleCredential.fromStream(bis)
        						.createScoped(Collections.singleton(DriveScopes.DRIVE));
        
        return new Drive.Builder(Utils.getDefaultTransport(), 
        					Utils.getDefaultJsonFactory(), credential)
        				.setApplicationName("Enablix")
        				.build();
	}
	
	
	public void printFiles(Drive drive) throws IOException {
		
		// Print the names and IDs for up to 10 files.
        FileList result = drive.files().list()
             .setPageSize(10)
             .setFields("nextPageToken, files(id, name)")
             .execute();
        
        List<File> files = result.getFiles();
        
        if (files == null || files.size() == 0) {
            
        	LOGGER.debug("No files found.");
            
        } else {
        	
            LOGGER.debug("Files:");
            
            for (File file : files) {
            	
                LOGGER.debug("{} ({})", file.getName(), file.getId());
                
                PermissionList permList = drive.permissions().list(file.getId())
                							   .setFields("permissions(id, role, emailAddress)")
                							   .execute();
                
                if (permList != null) {
	                for (Permission perm : permList.getPermissions()) {
	                	LOGGER.debug("Permission: {}", perm);
	                }
                }
            }
        }
	}
	
	public void shareFolderWithUser(Drive drive, String folderPath, String userEmailId) throws FileNotFoundException, IOException {
		File folder = findFolder(drive, folderPath);
		if (folder != null) {
			shareFolderIdWithUser(drive, folder.getId(), folder.getName(), userEmailId);
		}
	}
	
	public void shareFolderIdWithUser(Drive drive, String folderId, String folderName, String userEmailId) throws FileNotFoundException, IOException {

		LOGGER.debug("Sharing [{}] with [{}]", folderId, userEmailId);
		
		Permission perm = new Permission();
		perm.setType("user");
		perm.setRole("writer");
		perm.setEmailAddress(userEmailId);
		
		drive.permissions().create(folderId, perm).execute();
		
		EventUtil.publishEvent(new Event<GDriveShareVO>(Events.GDRIVE_FOLDER_SHARE, new GDriveShareVO(folderName, userEmailId)));
	}

	public void checkAndShareFolderWithUser(Drive drive, String folderPath, String userEmailId) throws FileNotFoundException, IOException {
		
		File folder = createOrFindFolderStructure(drive, folderPath);
		
		if (folder != null) {
			
			String folderId = folder.getId();
			PermissionList permList = drive.permissions().list(folderId)
										   .setFields("permissions(id, role, emailAddress)")
										   .execute();
			
			if (permList != null) {
			
				boolean userHasPerm = false;

				for (Permission perm : permList.getPermissions()) {
					
					if (userEmailId.equalsIgnoreCase(perm.getEmailAddress())) {
						
						LOGGER.debug("User [{}] has permission on [{}]", userEmailId, folderPath);
						userHasPerm = true;
						break;
					}
				}
				
				if (!userHasPerm) {
					shareFolderIdWithUser(drive, folderId, folder.getName(), userEmailId);
				}
			}
		}
	}
	
	@EventSubscription(eventName = Events.GDRIVE_FOLDER_SHARE)
	public void mailOnShare(GDriveShareVO shareVO) {
		
		Map<String, Object> inputData = new HashMap<>();
		inputData.put("sharedFolder", shareVO.folderName);
		
		Recipient recipient = new EmailRequest.Recipient();
		recipient.setEmailId(shareVO.getUserEmailId());
		
		EmailRequest emailRequest = new EmailRequest();
		emailRequest.setMailTemplateId("gdriveshare");
		emailRequest.setRecipients(Collections.singletonList(recipient));
		emailRequest.setInputData(inputData);
		
		emailProcessor.sendEmail(BasicEmailVelocityInputBuilder.MAIL_TYPE_GENERAL, emailRequest);
	}
	
	public void unshareFolderWithUser(Drive drive, String folderPath, String userEmailId) throws FileNotFoundException, IOException {
		
		File folder = findFolder(drive, folderPath);
		
		if (folder != null) {
			
			String folderId = folder.getId();
			PermissionList permList = drive.permissions().list(folderId)
										   .setFields("permissions(id, role, emailAddress)")
										   .execute();
			
			if (permList != null) {
			
				for (Permission perm : permList.getPermissions()) {
					
					if (userEmailId.equalsIgnoreCase(perm.getEmailAddress())) {
						
						LOGGER.debug("Un-sharing [{}] with [{}]", folderPath, userEmailId);
						
						drive.permissions().delete(folderId, perm.getId()).execute();
						break;
					}
				}
			}
		}
	}
	
	private File findFolder(Drive drive, String folderHierarchy) throws IOException {
		
		File folder = null;
		
		String[] folders = FileUtil.getFolderList(folderHierarchy);
		String folderParentId = null;
		
		for (String folderName : folders) {
			
			folder = findFolder(drive, folderName, folderParentId);
			if (folder == null) {
				break;
			}
			
			folderParentId = folder.getId();
		}
		
		return folder;
	}
	
	public File uploadFile(Drive drive, String destPath, String filename, 
			InputStream dataStream, String mimeType) throws FileNotFoundException, IOException {
		
		File file = new File();
		file.setName(filename);

		String parentFolderId = createOrFindFolderStructure(drive, destPath).getId();
		
		InputStreamContent fileContent = new InputStreamContent(mimeType, dataStream);
		
		File uploadedFile = null;
		
		File existingFile = findFile(drive, filename, parentFolderId);
		
		if (existingFile != null) {
			
			uploadedFile = drive.files().update(existingFile.getId(), file, fileContent)
										.setFields("id, name, parents").execute();
			
		} else {
			
			if (StringUtil.hasText(parentFolderId)) {
				file.setParents(Collections.singletonList(parentFolderId));
			}
			
			uploadedFile = drive.files().create(file, fileContent)
										.setFields("id, name, parents").execute();
		}
		
		return uploadedFile;
	}
	
	public void deleteFile(Drive drive, String filePath, String fileName) throws FileNotFoundException, IOException {
		
		findFileAndExecute(drive, filePath, fileName, (gDrive, file) -> { 
				gDrive.files().delete(file.getId()).execute();
			});
		
	}
	
	private static interface FileOperation {
		void execute(Drive drive, File file) throws IOException;
	}
	
	private <R> void findFileAndExecute(Drive drive, String filePath, String fileName, 
						FileOperation operation) throws FileNotFoundException, IOException {
		
		File parentFolder = findFolder(drive, filePath);
		
		if (parentFolder != null) {
			
			File file = findFile(drive, fileName, parentFolder.getId());
			
			if (file != null) {
				operation.execute(drive, file);
			}
		}
	}
	
	public File moveFile(Drive drive, String fromFilePath, String toFilePath, 
			String fileName) throws FileNotFoundException, IOException {
		
		File updatedFile = null;
		
		File parentFolder = findFolder(drive, fromFilePath);
		
		if (parentFolder != null) {
			
			String parentFolderId = parentFolder.getId();
			File file = findFile(drive, fileName, parentFolderId);
			
			if (file != null) {
				updatedFile = moveFileFromParent(drive, file.getId(), parentFolderId, toFilePath);
			}
		}
		
		return updatedFile;
	}
	
	public File moveFileFromParent(Drive drive, String fileId, String parentFolderId, 
			String toFilePath) throws FileNotFoundException, IOException {
		
		File updatedFile = null;
		
		if (StringUtil.hasText(fileId)) {
			
			String destFolderId = createOrFindFolderStructure(drive, toFilePath).getId();
			
			updatedFile = drive.files().update(fileId, null)
								 	   .setAddParents(destFolderId)
								 	   .setRemoveParents(parentFolderId)
								 	   .setFields("id, name, parents")
								 	   .execute();
		}
		
		return updatedFile;
	}
	
	public File createOrFindFolderStructure(Drive drive, String folderPath) throws IOException {
		
		String[] folders = FileUtil.getFolderList(folderPath);
		
		String parentFolderId = null;
		File parentFolder = null;
		
		for (String currFolder : folders) {
		
			File folder = findFolder(drive, currFolder, parentFolderId);
			
			if (folder == null) {
				folder = createFolder(drive, currFolder, parentFolderId);
			}
			
			parentFolderId = folder.getId();
			parentFolder = folder;
		}
		
		return parentFolder;
	}
	
	public String findFolderStructure(Drive drive, String folderPath) throws IOException {
		
		String[] folders = FileUtil.getFolderList(folderPath);
		
		String parentFolderId = null;
		
		for (String currFolder : folders) {
		
			File folder = findFolder(drive, currFolder, parentFolderId);
			
			if (folder == null) {
				return null;
			}
			
			parentFolderId = folder.getId();
		}
		
		return parentFolderId;
	}
	
	private File findFolder(Drive drive, String folderName, String parentId) throws IOException {
		
		File folder = null;
		StringBuilder query = new StringBuilder();
		
		query.append("mimeType = '").append(MIME_TYPE_GOOGLE_APPS_FOLDER).append("' ")
			 .append("and name = '").append(folderName).append("' ");
		
		if (StringUtil.hasText(parentId)) {
			query.append("and '").append(parentId).append("' in parents");
		}
		
		FileList folders = drive.files().list()
								.setQ(query.toString())
								.setSpaces("drive")
								.setFields("files(id, name)")
								.execute();
		
		if (folders != null) {
		
			List<File> folderList = folders.getFiles();
			if (CollectionUtil.isNotEmpty(folderList)) {
				folder = folderList.get(0);
			}
		}
		
		return folder;
	}
	
	private File findFile(Drive drive, String fileName, String parentId) throws IOException {
		
		File file = null;
		StringBuilder query = new StringBuilder();
		
		query.append("name = '").append(fileName).append("' ");
		
		if (StringUtil.hasText(parentId)) {
			query.append("and '").append(parentId).append("' in parents");
		}
		
		FileList folders = drive.files().list()
								.setQ(query.toString())
								.setSpaces("drive")
								.setFields("files(id, name, parents)")
								.execute();
		
		if (folders != null) {
		
			List<File> folderList = folders.getFiles();
			if (CollectionUtil.isNotEmpty(folderList)) {
				file = folderList.get(0);
			}
		}
		
		return file;
	}
	
	
	private File createFolder(Drive drive, String folderName, String parentId) throws IOException {
		
		File folder = new File();
		folder.setName(folderName);
		folder.setMimeType(MIME_TYPE_GOOGLE_APPS_FOLDER);
		
		if (StringUtil.hasText(parentId)) {
			folder.setParents(Collections.singletonList(parentId));
		}
		
		File newFolder = drive.files().create(folder).setFields("id").execute();
		return newFolder;
	}


	public void deleteFile(Drive drive, String fileId) throws IOException {
		drive.files().delete(fileId).execute();
	}
	
	public static class GDriveShareVO {

		private String folderName;
		private String userEmailId;
		
		public GDriveShareVO() { }

		public GDriveShareVO(String folderName, String userEmailId) {
			super();
			this.folderName = folderName;
			this.userEmailId = userEmailId;
		}

		public String getFolderName() {
			return folderName;
		}

		public void setFolderName(String folderName) {
			this.folderName = folderName;
		}

		public String getUserEmailId() {
			return userEmailId;
		}

		public void setUserEmailId(String userEmailId) {
			this.userEmailId = userEmailId;
		}
		
	}
}
