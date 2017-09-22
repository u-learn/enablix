package com.enablix.commons.dms.google.drive;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import com.enablix.commons.util.FileUtil;
import com.enablix.commons.util.StringUtil;
import com.enablix.commons.util.collection.CollectionUtil;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.util.Utils;
import com.google.api.client.http.FileContent;
import com.google.api.client.util.IOUtils;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.Permission;
import com.google.api.services.drive.model.PermissionList;

public class GoogleDriveAPI {

	private static final String MIME_TYPE_GOOGLE_APPS_FOLDER = "application/vnd.google-apps.folder";
	private static final String AUTH_CONFIG_KEY_FILE = "D:\\enablix\\google-drive-integration\\Enablix-Local-baac50ab6372.json";
	
	private Drive getDriveService() throws FileNotFoundException, IOException {
        
		GoogleCredential credential = 
        		GoogleCredential.fromStream(new FileInputStream(AUTH_CONFIG_KEY_FILE))
        						.createScoped(Collections.singleton(DriveScopes.DRIVE));
        
        return new Drive.Builder(Utils.getDefaultTransport(), 
        					Utils.getDefaultJsonFactory(), credential)
        				.setApplicationName("Enablix")
        				.build();
	}
	
	
	public void printFiles() throws IOException {
		
		Drive service = getDriveService();
		
		// Print the names and IDs for up to 10 files.
        FileList result = service.files().list()
             .setPageSize(10)
             .setFields("nextPageToken, files(id, name)")
             .execute();
        
        List<File> files = result.getFiles();
        
        if (files == null || files.size() == 0) {
            
        	System.out.println("No files found.");
            
        } else {
        	
            System.out.println("Files:");
            for (File file : files) {
                System.out.printf("%s (%s)\n", file.getName(), file.getId());
                
                PermissionList permList = service.permissions().list(file.getId()).setFields("permissions(id, role, emailAddress)").execute();
                if (permList != null) {
	                for (Permission perm : permList.getPermissions()) {
	                	System.out.printf("Permission: %s \n", perm);
	                }
                }
            }
        }
	}
	
	public void shareFolderWithUser(String folderPath, String userEmailId) throws FileNotFoundException, IOException {
		
		Drive service = getDriveService();
		String folderId = findFolder(folderPath, service);
		
		if (StringUtil.hasText(folderId)) {
			
			Permission perm = new Permission();
			perm.setType("user");
			perm.setRole("writer");
			perm.setEmailAddress(userEmailId);
			
			service.permissions().create(folderId, perm).execute();
		}
		
	}
	
	public void unshareFolderWithUser(String folderPath, String userEmailId) throws FileNotFoundException, IOException {
		
		Drive service = getDriveService();
		String folderId = findFolder(folderPath, service);
		
		if (StringUtil.hasText(folderId)) {
			
			PermissionList permList = service.permissions().list(folderId).setFields("permissions(id, role, emailAddress)").execute();
			
			if (permList != null) {
			
				for (Permission perm : permList.getPermissions()) {
					
					if (userEmailId.equalsIgnoreCase(perm.getEmailAddress())) {
						service.permissions().delete(folderId, perm.getId()).execute();
						break;
					}
				}
			}
		}
	}
	
	private String findFolder(String folderHierarchy, Drive drive) throws IOException {
		
		String folderId = null;
		
		String[] folders = FileUtil.getFolderList(folderHierarchy);
		String folderParentId = null;
		
		for (String folder : folders) {
			folderId = findFolder(folder, folderParentId, drive);
			if (!StringUtil.hasText(folderId)) {
				break;
			}
			folderParentId = folderId;
		}
		
		return folderId;
	}
	
	public File uploadFile(String destPath, String sourcePath, String mimeType) throws FileNotFoundException, IOException {
		
		Drive service = getDriveService();
		
		java.io.File sourcefile = new java.io.File(sourcePath);
		
		File file = new File();
		file.setName(sourcefile.getName());

		String parentFolderId = createOrFindFolderStructure(destPath, service);
		
		FileContent fileContent = new FileContent(mimeType, sourcefile);
		
		File uploadedFile = null;
		
		File existingFile = findFile(sourcefile.getName(), parentFolderId, service);
		
		if (existingFile != null) {
			
			uploadedFile = service.files().update(existingFile.getId(), file, fileContent).execute();
			
		} else {
			
			if (StringUtil.hasText(parentFolderId)) {
				file.setParents(Collections.singletonList(parentFolderId));
			}
			
			uploadedFile = service.files().create(file, fileContent).execute();
		}
		
		return uploadedFile;
	}
	
	public void deleteFile(String filePath, String fileName) throws FileNotFoundException, IOException {
		
		findFileAndExecute(filePath, fileName, (file, gDrive) -> { 
				gDrive.files().delete(file.getId()).execute();
			}
		);
		
	}
	
	private static interface FileOperation {
		void execute(File file, Drive drive) throws IOException;
	}
	
	private <R> void findFileAndExecute(String filePath, String fileName, 
						FileOperation operation) throws FileNotFoundException, IOException {
		
		Drive drive = getDriveService();
		String parentFolderId = findFolder(filePath, drive);
		
		if (StringUtil.hasText(parentFolderId)) {
			
			File file = findFile(fileName, parentFolderId, drive);
			
			if (file != null) {
				operation.execute(file, drive);
			}
		}
	}
	
	public void moveFile(String fromFilePath, String toFilePath, String fileName) throws FileNotFoundException, IOException {
		
		Drive drive = getDriveService();
		String parentFolderId = findFolder(fromFilePath, drive);
		
		if (StringUtil.hasText(parentFolderId)) {
			
			File file = findFile(fileName, parentFolderId, drive);
			
			if (file != null) {
				
				String destFolderId = createOrFindFolderStructure(toFilePath, drive);
				
				drive.files().update(file.getId(), null)
							 .setAddParents(destFolderId)
							 .setRemoveParents(parentFolderId)
							 .setFields("id, name, parents")
							 .execute();
			}
		}
		
	}
	
	private String createOrFindFolderStructure(String folderPath, Drive drive) throws IOException {
		
		String[] folders = FileUtil.getFolderList(folderPath);
		
		String parentFolderId = null;
		
		for (String currFolder : folders) {
		
			String folderId = findFolder(currFolder, parentFolderId, drive);
			
			if (StringUtil.isEmpty(folderId)) {
				folderId = createFolder(currFolder, parentFolderId, drive);
			}
			
			parentFolderId = folderId;
		}
		
		return parentFolderId;
	}
	
	private String findFolder(String folderName, String parentId, Drive drive) throws IOException {
		
		String folderId = null;
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
				folderId = folderList.get(0).getId();
			}
		}
		
		return folderId;
	}
	
	private File findFile(String fileName, String parentId, Drive drive) throws IOException {
		
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
	
	
	private String createFolder(String folderName, String parentId, Drive drive) throws IOException {
		
		File folder = new File();
		folder.setName(folderName);
		folder.setMimeType(MIME_TYPE_GOOGLE_APPS_FOLDER);
		
		if (StringUtil.hasText(parentId)) {
			folder.setParents(Collections.singletonList(parentId));
		}
		
		File newFolder = drive.files().create(folder).setFields("id").execute();
		return newFolder.getId();
	}
	
	public static void main(String[] args) throws IOException {
		
		GoogleDriveAPI api = new GoogleDriveAPI();
		
		//api.uploadFile("Enablix/Test/", "C:\\Users\\dluthra\\Downloads\\Brief.pdf", "application/pdf");
		
		//api.shareFolderWithUser("Enablix", "dluthra82@gmail.com");
		
		//api.unshareFolderWithUser("Enablix", "dluthra82@gmail.com");
		
		//api.deleteFile("Enablix/Test", "Brief.pdf");
		
		// api.moveFile("Enablix/Test", "Enablix/Test/tmp", "Brief(1).pdf");
		
		IOUtils.copy(api.downloadFile("0Bw2oRN3e3b_TRG9BWndNS0pJSGM"), 
				new FileOutputStream(new java.io.File("C:\\Users\\dluthra\\Downloads\\download-test2.pdf")));
		
		api.printFiles();
	}


	private InputStream downloadFile(String fileId) throws IOException {
		Drive drive = getDriveService();
		return drive.files().get(fileId).executeMediaAsInputStream();
	}
	
}
