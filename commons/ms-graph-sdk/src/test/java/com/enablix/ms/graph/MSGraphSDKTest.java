package com.enablix.ms.graph;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.util.FileCopyUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.enablix.ms.graph.impl.AppLoginHandler;
import com.enablix.ms.graph.impl.FileUploadHandler;
import com.enablix.ms.graph.impl.MSGraphSDKImpl;

public class MSGraphSDKTest {

	public static void main(String[] args) throws MSGraphException, IOException {
		
		String clientId = "d9587371-5577-4815-8be0-a945cee3ce11";
		String clientSecret = "qufzFLISX30%ogoKE734[%$";
		String orgId = "enablix.onmicrosoft.com";
		String driveOwnerId = "admin@enablix.onmicrosoft.com";
		
		try {
			
			RestTemplate restTemplate = new RestTemplate();
			MSGraphSDK sdk = MSGraphSDKImpl.create(AppLoginHandler.create(restTemplate), restTemplate, FileUploadHandler.create(restTemplate));
			
			MSGraphSession session = sdk.loginAsApp(clientId, clientSecret, orgId, driveOwnerId);
			
			/** Post file **/
			/*
			String filePath = "C:\\Users\\dluthra\\Downloads\\Brief.pdf";
			File file = new File(filePath);
			FileInputStream fis = new FileInputStream(file);
			
			String uploadPath = "Enablix/Case Studies";
			String filename = "Brief.pdf";
			
			OneDriveFile uploadFile = sdk.uploadFile(session, uploadPath, filename, fis, file.length());
			System.out.println(uploadFile);
			*/
			/** Post file **/
			
			/** Delete file **/
			/*
			String fileId = "01GP3KYYVAINUPX2FHKZBL45IWTZPXHSUC";
			sdk.deleteFile(session, fileId);
			*/
			/** Delete file end **/
			
			/** Move file **/
			/*
			String fileItemId = "01GP3KYYSBA32PL5E6OVBJ2GLT223CML7Q";
			String moveToFileLocation  = "Enablix/Test5";
			String filename = "Brief.pdf";
			OneDriveFolder destFolder = sdk.moveFile(session, fileItemId, moveToFileLocation, filename);
			System.out.println(destFolder);
			*/
			/** Move file end **/
			
			/** Download file **/
			File destFile = new File("D:\\enablix\\onedrive\\abc.pdf");
			if (destFile.exists()) {
				destFile.delete();
			}
			destFile.createNewFile();
			
			String fileItemId = "01GP3KYYSBA32PL5E6OVBJ2GLT223CML7Q";
			InputStream filestream = sdk.getFileStreamById(session, fileItemId);
			FileOutputStream fos = new FileOutputStream(destFile);
			FileCopyUtils.copy(filestream, fos);
			
			filestream.close();
			fos.close();
			/** Download file end **/
			
		} catch (HttpClientErrorException ce) {
			ce.printStackTrace();
		}
		
	}
	
}
