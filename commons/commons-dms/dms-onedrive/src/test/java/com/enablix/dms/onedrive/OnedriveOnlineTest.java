package com.enablix.dms.onedrive;

import java.io.InputStream;
import java.net.URI;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.enablix.commons.util.web.WebUtils;

public class OnedriveOnlineTest {

	private RestTemplate restTemplate = new RestTemplate();
	
	
	public String receiveSecurityToken() throws Exception {
	  return "eyJ0eXAiOiJKV1QiLCJub25jZSI6IkFRQUJBQUFBQUFEWDhHQ2k2SnM2U0s4MlRzRDJQYjdyZWxnSlBGendUZGVITGpjSFpHZGl5MGYzODRjOUhUb0I1ZExjel9GZGZFeWdIOUZ2d1VWdEh2Z0F2NlU1a29YSHRubXFITGRNOUQzU2ZvWlRaUWthaUNBQSIsImFsZyI6IlJTMjU2IiwieDV0IjoiVGlvR3l3d2xodmRGYlhaODEzV3BQYXk5QWxVIiwia2lkIjoiVGlvR3l3d2xodmRGYlhaODEzV3BQYXk5QWxVIn0.eyJhdWQiOiJodHRwczovL2dyYXBoLm1pY3Jvc29mdC5jb20iLCJpc3MiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC80NjhiOTZmYy1mYWQyLTRjY2ItYTkzMi04OTQwYmU4NzU1MjYvIiwiaWF0IjoxNTMwMjcxOTI5LCJuYmYiOjE1MzAyNzE5MjksImV4cCI6MTUzMDI3NTgyOSwiYWlvIjoiWTJkZ1lCQ3B2bGJVb2JoNlU5bTN4MnYrK2oyMEFBQT0iLCJhcHBfZGlzcGxheW5hbWUiOiJFbmFibGl4IFRlc3QiLCJhcHBpZCI6ImQ5NTg3MzcxLTU1NzctNDgxNS04YmUwLWE5NDVjZWUzY2UxMSIsImFwcGlkYWNyIjoiMSIsImlkcCI6Imh0dHBzOi8vc3RzLndpbmRvd3MubmV0LzQ2OGI5NmZjLWZhZDItNGNjYi1hOTMyLTg5NDBiZTg3NTUyNi8iLCJvaWQiOiIxMmNiYWFhMi1hYTJhLTQzZjktOWUxZS03NTIxODIyNzg5ZGYiLCJyb2xlcyI6WyJGaWxlcy5SZWFkV3JpdGUuQWxsIl0sInN1YiI6IjEyY2JhYWEyLWFhMmEtNDNmOS05ZTFlLTc1MjE4MjI3ODlkZiIsInRpZCI6IjQ2OGI5NmZjLWZhZDItNGNjYi1hOTMyLTg5NDBiZTg3NTUyNiIsInV0aSI6InhkRzVWMVJKTVVlR0l0WDZjRTRPQUEiLCJ2ZXIiOiIxLjAifQ.AYjrCo4TvkPyCM-2Uvkt_Prua0C8bzcoYzoxc0MjmMrDNKLdxzHLint6l-eoLwWAeyH9_hxFiJblU1OprRqjjqU39ANAPDuZNbJdVEbKnZf27EhqgerfIkU8bSel_iuFoXFgBINNmoltE4fr7CtBhuKjFc4CZ3FRYQJ2eQqPWM7s95PG83lcdFADnFc6_d7RirZVqf4LCpwi1NGuSygZ1n0I7uKkjW_uPrvGczI2qfZ6JXGDT4kHiDRMP9-AkB0PV4J0C3LqLTUYtVBGxcoGojMsjLp3Ulwm4ORjGMTV2l7Scu3K2QZs0QdHjnK5ryijrqTT_m-HYg-3mEc9MSad3Q";
	}
	
	public String performHttpRequest(String path, String json, boolean isUpdate) throws Exception {
		  String securityToken = receiveSecurityToken();
		  MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		  headers.add("Content-type", "application/json");
		  headers.add("Authorization", securityToken);
		  RequestEntity<String> requestEntity = new RequestEntity<>(json, headers, HttpMethod.POST, new URI(path));
		  ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);
		  return responseEntity.getBody();
		}
	
	public String postFile(InputStream is, String path, boolean isUpdate) throws Exception {
		 String securityToken = receiveSecurityToken();
		  MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		  headers.add("Content-type", "application/json");
		  headers.add("Authorization", securityToken);
		  
		  InputStreamResource isResource = new InputStreamResource(is);
		  RequestEntity<InputStreamResource> requestEntity = new RequestEntity<InputStreamResource>(isResource, headers, HttpMethod.POST, new URI(path));
		  //RequestEntity<String> requestEntity = new RequestEntity<>(json, headers, HttpMethod.POST, new URI(path));
		  ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);
		  return responseEntity.getBody();
	}
	
	public String getFolder() throws Exception {
		String securityToken = receiveSecurityToken();
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		  headers.add("Content-type", "application/json");
		  headers.add("Authorization", securityToken);
		  
		  // https://enablix.sharepoint.com/sites/enablix-team/_api/Web/GetFolderByServerRelativeUrl('/sites/enablix-team/Shared%20Documents/Dikshit%20Local%20Env')/Files
		  RequestEntity<String> request = new RequestEntity<>(headers, HttpMethod.GET, new URI("https://enablix.sharepoint.com/_api/Web/GetFolderByServerRelativeUrl('/Shared%20Documents')"));
		  ResponseEntity<String> response = restTemplate.exchange(request, String.class);
		  return response.getBody();
	}

	public String deleteFile(String path) throws Exception {
		 String securityToken = receiveSecurityToken();
		 MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		  headers.add("Content-type", "application/json");
		  headers.add("Authorization", securityToken);
		  
		  RequestEntity<String> requestEntity = new RequestEntity<String>(null, headers, HttpMethod.POST, new URI(path));
		  //RequestEntity<String> requestEntity = new RequestEntity<>(json, headers, HttpMethod.POST, new URI(path));
		  ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);
		  return responseEntity.getBody();
	}
	
	public String createFolder(String path, String folderPath) throws Exception {
		 String securityToken = receiveSecurityToken();
		 MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		  headers.add("Content-type", "application/json");
		  headers.add("Authorization", securityToken);
		  
		  String jsonBody = "{ '__metadata': { 'type': 'SP.Folder' }, 'ServerRelativeUrl': '" + folderPath + "'}";
		  System.out.println("Create folder json (main) : " + jsonBody);
		  System.out.println("Create folder uri (main) : " + path);
		  RequestEntity<String> requestEntity = new RequestEntity<String>(jsonBody, headers, HttpMethod.POST, new URI(path));
		  ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);
		  return responseEntity.getBody();
	}
	
	public InputStream downloadFile(String filepath) throws Exception {
		String securityToken = receiveSecurityToken();
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		  headers.add("Content-type", "application/json");
		  headers.add("Authorization", securityToken);
		  
		  CloseableHttpClient httpClient = HttpClients.createDefault();
		  HttpGet request = new HttpGet(filepath);
		  
		  CloseableHttpResponse response = httpClient.execute(request);
		  return response.getEntity().getContent();
	}
	
	public String moveFile(String currentFileLocation, String moveToFileLocation) throws Exception {
		String securityToken = receiveSecurityToken();
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		  headers.add("Content-type", "application/json");
		  headers.add("Authorization", securityToken);
		  
		  String path = "https://enablix.sharepoint.com/_api/web/GetFileByServerRelativeUrl('" 
				  			+ WebUtils.sanitizeURI(currentFileLocation) + "')/moveto(newurl='" 
				  			+ WebUtils.sanitizeURI(moveToFileLocation) + "',flags=1)"; 
		  
		  System.out.println(path);
		  RequestEntity<String> requestEntity = new RequestEntity<String>(null, headers, HttpMethod.POST, new URI(path));
		  ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);
		  return responseEntity.getBody();
	}
	
	public String getFileDetail(String filepath) throws Exception {
		String uri = "https://enablix.sharepoint.com/_api/Web/GetFileByServerRelativeUrl('" + filepath + "')";
		String securityToken = receiveSecurityToken();
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		  headers.add("Content-type", "application/json");
		  headers.add("Authorization", securityToken);
		  
		  // https://enablix.sharepoint.com/sites/enablix-team/_api/Web/GetFolderByServerRelativeUrl('/sites/enablix-team/Shared%20Documents/Dikshit%20Local%20Env')/Files
		  RequestEntity<String> request = new RequestEntity<>(headers, HttpMethod.GET, new URI(uri));
		  ResponseEntity<String> response = restTemplate.exchange(request, String.class);
		  return response.getBody();
	}
	
	public static void main(String[] args) throws Exception {
		try {
		OnedriveOnlineTest auth = new OnedriveOnlineTest();
		//String returnVal = auth.getFolder();
		
		/** Post file **/
		/*
		String filePath = "C:\\Users\\dluthra\\Downloads\\Brief.pdf";
		FileInputStream fis = new FileInputStream(filePath);
		
		String endpoint = "https://enablix.sharepoint.com/_api/Web/GetFolderByServerRelativeUrl('/Shared%20Documents/Dikshit%20Local%20Env')/Files/add(url='Brief1.pdf',overwrite=true)";
		String returnVal = auth.postFile(fis, endpoint, false);
		*/
		/** Post file end **/
		
		/** Delete file **/
		/*
		String filepath = "/Shared%20Documents/Dikshit%20Local%20Env/Brief.pdf";
		//String path = "https://enablix.sharepoint.com/_api/Web/GetFileByServerRelativeUrl('/Shared Documents/Dikshit Local Env/Brief.pdf')";
		//filepath = URLEncoder.encode(filepath, "UTF-8");
		System.out.println(filepath);
		
		String path = "https://enablix.sharepoint.com/_api/Web/GetFileByServerRelativeUrl('" + filepath + "')";
		String returnVal = auth.deleteFile(path);
		*/
		/** Delete file end **/
		
		/** Create folder **/
		/*
		String uri = "https://enablix.sharepoint.com/_api/web/folders";
		//String folderPath = "/Shared Documents/Dikshit Test/New Folder";
		String folderPath = "/Shared Documents/Dikshit Local Env/Customer 1 Case study";
		String returnVal = auth.createFolder(uri, folderPath);
		*/
		/** Create folder end **/
		
		/** Download file **/
		/*
		String filepath = "https://enablix.sharepoint.com/_api/Web/GetFileByServerRelativeUrl('/Shared%20Documents/Dikshit%20Local%20Env/Brief.pdf')/$value";
		InputStream filestream = auth.downloadFile(filepath);
		FileOutputStream fos = new FileOutputStream(new File("D:\\enablix\\sharepoint\\abc.pdf"));
		FileCopyUtils.copy(filestream, fos);
		
		filestream.close();
		fos.close();
		*/
		/** Download file end **/
		
		/** Move file **/
		/*
		String currentFileLocation = "/Shared Documents/Dikshit Local Env/Brief1.pdf";
		String moveToFileLocation  = "/Shared Documents/Dikshit Test1/Brief.pdf";
		String returnVal = auth.moveFile(currentFileLocation, moveToFileLocation);
		*/
		/** Move file end **/
		
		/** Get File detail **/
		String filepath = "/Shared%20Documents/eOriginal%20Test%20Env/Presentations/Sample%20PPT.pptx";
		String returnVal = auth.getFileDetail(filepath);
		/** Get File detail end **/
		
		System.out.println(returnVal);
		} catch (HttpClientErrorException | HttpServerErrorException e) {
			
			System.out.println("Error fetching folder details: " + e.getMessage());
			e.printStackTrace();
		}
		
	}
	
}
