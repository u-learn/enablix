package com.enablix.dms.sharepoint;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.xml.transform.StringSource;
import org.springframework.xml.xpath.XPathExpression;
import org.springframework.xml.xpath.XPathExpressionFactory;
import org.w3c.dom.Document;

import com.enablix.commons.util.json.JsonUtil;
import com.enablix.commons.util.web.WebUtils;
import com.google.common.base.Joiner;

public class SharepointOnlineTest {

	private static Map<String, String> namespaces;
	
	static {
		namespaces = new HashMap<>();
		namespaces.put("S", "http://www.w3.org/2003/05/soap-envelope");
		namespaces.put("wsse", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd");
		namespaces.put("wsu", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd");
		namespaces.put("wsa", "http://www.w3.org/2005/08/addressing");
		namespaces.put("wst", "http://schemas.xmlsoap.org/ws/2005/02/trust");
	}
	
	private RestTemplate restTemplate = new RestTemplate();
	
	private XPathExpression xPathExpression = XPathExpressionFactory.createXPathExpression(
			"/S:Envelope/S:Body/wst:RequestSecurityTokenResponse/wst:RequestedSecurityToken/wsse:BinarySecurityToken", namespaces);
	
	
	public String receiveSecurityToken() throws Exception {
	  String tokenRequest = buildSecurityTokenRequestEnvelope();
	RequestEntity<String> requestEntity = new RequestEntity<>(tokenRequest, 
			  HttpMethod.POST, new URI("https://login.microsoftonline.com/extSTS.srf"));
	  ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);
	  DOMResult result = new DOMResult();
	  Transformer transformer = TransformerFactory.newInstance().newTransformer();
	  transformer.transform(new StringSource(responseEntity.getBody()), result);
	  Document definitionDocument = (Document) result.getNode();
	  String securityToken = xPathExpression.evaluateAsString(definitionDocument);
	  if (!StringUtils.hasText(securityToken)) { 
	    throw new Exception("Unable to authenticate: empty token");
	  }
	  return securityToken;
	}
	
	public List<String> getSignInCookies(String securityToken) throws TransformerException, URISyntaxException {
		  RequestEntity<String> requestEntity = new RequestEntity<>(securityToken, HttpMethod.POST, new URI("https://enablix.sharepoint.com/_forms/default.aspx?wa=wsignin1.0"));
		  ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);
		  HttpHeaders headers = responseEntity.getHeaders();
		  List<String> cookies = headers.get("Set-Cookie");
		  if (CollectionUtils.isEmpty(cookies)) {
		    throw new IllegalStateException("Unable to sign in: no cookies returned in response");
		  } 
		  return cookies;
		}
	
	public String getFormDigestValue(List<String> cookies) throws IOException, URISyntaxException, TransformerException {
		  MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		  headers.add("Cookie", Joiner.on(';').join(cookies));
		  headers.add("Accept", "application/json;odata=verbose");
		  headers.add("X-ClientService-ClientTag", "SDK-JAVA");
		  RequestEntity<String> requestEntity = new RequestEntity<>(headers, HttpMethod.POST, 
				  new URI("https://enablix.sharepoint.com/_api/contextinfo"));
		  ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);
		  System.out.println(responseEntity.getBody());
		  return (String) JsonUtil.getJsonpathValue(responseEntity.getBody(), "d.GetContextWebInformation.FormDigestValue");
		}
	
	public String performHttpRequest(String path, String json, boolean isUpdate) throws Exception {
		  String securityToken = receiveSecurityToken();
		  List<String> cookies = getSignInCookies(securityToken);
		  String formDigestValue = getFormDigestValue(cookies);
		  MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		  headers.add("Cookie", Joiner.on(';').join(cookies));
		  headers.add("Content-type", "application/json;odata=verbose");
		  headers.add("X-RequestDigest", formDigestValue);
		  if (isUpdate) {
		    headers.add("X-HTTP-Method", "MERGE");
		    headers.add("IF-MATCH", "*");
		  }
		  RequestEntity<String> requestEntity = new RequestEntity<>(json, headers, HttpMethod.POST, new URI(path));
		  ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);
		  return responseEntity.getBody();
		}
	
	public String postFile(InputStream is, String path, boolean isUpdate) throws Exception {
		 String securityToken = receiveSecurityToken();
		  List<String> cookies = getSignInCookies(securityToken);
		  String formDigestValue = getFormDigestValue(cookies);
		  MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		  headers.add("Cookie", Joiner.on(';').join(cookies));
		  headers.add("Content-type", "application/json;odata=verbose");
		  headers.add("X-RequestDigest", formDigestValue);
		  if (isUpdate) {
		    headers.add("X-HTTP-Method", "MERGE");
		    headers.add("IF-MATCH", "*");
		  }
		  
		  InputStreamResource isResource = new InputStreamResource(is);
		  RequestEntity<InputStreamResource> requestEntity = new RequestEntity<InputStreamResource>(isResource, headers, HttpMethod.POST, new URI(path));
		  //RequestEntity<String> requestEntity = new RequestEntity<>(json, headers, HttpMethod.POST, new URI(path));
		  ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);
		  return responseEntity.getBody();
	}
	
	public String getFolder() throws Exception {
		String securityToken = receiveSecurityToken();
		  List<String> cookies = getSignInCookies(securityToken);
		  String formDigestValue = getFormDigestValue(cookies);
		  MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		  headers.add("Cookie", Joiner.on(';').join(cookies));
		  headers.add("Content-type", "application/json;odata=verbose");
		  headers.add("X-RequestDigest", formDigestValue);
		  
		  headers.add("Accept", "application/json;odata=verbose");
		  
		  // https://enablix.sharepoint.com/sites/enablix-team/_api/Web/GetFolderByServerRelativeUrl('/sites/enablix-team/Shared%20Documents/Dikshit%20Local%20Env')/Files
		  RequestEntity<String> request = new RequestEntity<>(headers, HttpMethod.GET, new URI("https://enablix.sharepoint.com/_api/Web/GetFolderByServerRelativeUrl('/Shared%20Documents')"));
		  ResponseEntity<String> response = restTemplate.exchange(request, String.class);
		  return response.getBody();
	}

	private String buildSecurityTokenRequestEnvelope() {
		
		return "<s:Envelope xmlns:s=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:a=\"http://www.w3.org/2005/08/addressing\">" +
					"<s:Header>" +
						"<a:Action s:mustUnderstand=\"1\">http://schemas.xmlsoap.org/ws/2005/02/trust/RST/Issue</a:Action>" + 
						"<a:ReplyTo>" +
							"<a:Address>http://www.w3.org/2005/08/addressing/anonymous</a:Address>" +
						"</a:ReplyTo>" + 
						"<a:To s:mustUnderstand=\"1\">https://login.microsoftonline.com/extSTS.srf</a:To>" + 
						"<o:Security s:mustUnderstand=\"1\" xmlns:o=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\">" +
							"<o:UsernameToken>" +
								"<o:Username>admin@enablix.onmicrosoft.com</o:Username>" +
								"<o:Password>Enablixis1</o:Password>" +
							"</o:UsernameToken>" +
						"</o:Security>" +
					"</s:Header>" +
					"<s:Body>" +
						"<t:RequestSecurityToken xmlns:t=\"http://schemas.xmlsoap.org/ws/2005/02/trust\">" +
							"<wsp:AppliesTo xmlns:wsp=\"http://schemas.xmlsoap.org/ws/2004/09/policy\">" +
								"<a:EndpointReference>" +
									"<a:Address>https://enablix.sharepoint.com</a:Address>" +
								"</a:EndpointReference>" +
							"</wsp:AppliesTo>" + 
							"<t:KeyType>http://schemas.xmlsoap.org/ws/2005/05/identity/NoProofKey</t:KeyType>" +
							"<t:RequestType>http://schemas.xmlsoap.org/ws/2005/02/trust/Issue</t:RequestType>" +
							"<t:TokenType>urn:oasis:names:tc:SAML:1.0:assertion</t:TokenType>" +
						"</t:RequestSecurityToken>" +
					"</s:Body>" +
				"</s:Envelope>";
	}
	
	public String deleteFile(String path) throws Exception {
		 String securityToken = receiveSecurityToken();
		  List<String> cookies = getSignInCookies(securityToken);
		  String formDigestValue = getFormDigestValue(cookies);
		  MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		  headers.add("Cookie", Joiner.on(';').join(cookies));
		  headers.add("Content-type", "application/json;odata=verbose");
		  headers.add("X-RequestDigest", formDigestValue);
		  headers.add("X-HTTP-Method", "DELETE");
		  headers.add("IF-MATCH", "*");
		  
		  RequestEntity<String> requestEntity = new RequestEntity<String>(null, headers, HttpMethod.POST, new URI(path));
		  //RequestEntity<String> requestEntity = new RequestEntity<>(json, headers, HttpMethod.POST, new URI(path));
		  ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);
		  return responseEntity.getBody();
	}
	
	public String createFolder(String path, String folderPath) throws Exception {
		 String securityToken = receiveSecurityToken();
		  List<String> cookies = getSignInCookies(securityToken);
		  String formDigestValue = getFormDigestValue(cookies);
		  MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		  headers.add("Cookie", Joiner.on(';').join(cookies));
		  headers.add("Content-type", "application/json;odata=verbose");
		  headers.add("Accept", "application/json;odata=verbose");
		  headers.add("X-RequestDigest", formDigestValue);
		  
		  String jsonBody = "{ '__metadata': { 'type': 'SP.Folder' }, 'ServerRelativeUrl': '" + folderPath + "'}";
		  System.out.println("Create folder json (main) : " + jsonBody);
		  System.out.println("Create folder uri (main) : " + path);
		  RequestEntity<String> requestEntity = new RequestEntity<String>(jsonBody, headers, HttpMethod.POST, new URI(path));
		  ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);
		  return responseEntity.getBody();
	}
	
	public InputStream downloadFile(String filepath) throws Exception {
		String securityToken = receiveSecurityToken();
		 List<String> cookies = getSignInCookies(securityToken);
		  String formDigestValue = getFormDigestValue(cookies);
		  
		  /* Testing if two call can be made using same cookies and form digest
		  MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		  headers.add("Cookie", Joiner.on(';').join(cookies));
		  headers.add("Content-type", "application/json;odata=verbose");
		  headers.add("Accept", "application/json;odata=verbose");
		  headers.add("X-RequestDigest", formDigestValue);
		  
		  String uri = "https://enablix.sharepoint.com/_api/web/folders";
		  String folderPath = "/Shared Documents/Dikshit Test/New Folder2";
		  String jsonBody = "{ '__metadata': { 'type': 'SP.Folder' }, 'ServerRelativeUrl': '" + folderPath + "'}";
		  RequestEntity<String> requestEntity = new RequestEntity<String>(jsonBody, headers, HttpMethod.POST, new URI(uri));
		  ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);
		  System.out.println(responseEntity.getBody());
		  */
		  
		  CloseableHttpClient httpClient = HttpClients.createDefault();
		  HttpGet request = new HttpGet(filepath);
		  
		  request.addHeader("Cookie", Joiner.on(';').join(cookies));
		  request.addHeader("X-RequestDigest", formDigestValue);
		  
		  CloseableHttpResponse response = httpClient.execute(request);
		  return response.getEntity().getContent();
	}
	
	public String moveFile(String currentFileLocation, String moveToFileLocation) throws Exception {
		String securityToken = receiveSecurityToken();
		  List<String> cookies = getSignInCookies(securityToken);
		  String formDigestValue = getFormDigestValue(cookies);
		  MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		  headers.add("Cookie", Joiner.on(';').join(cookies));
		  headers.add("Content-type", "application/json;odata=verbose");
		  headers.add("Accept", "application/json;odata=verbose");
		  headers.add("X-RequestDigest", formDigestValue);
		  
		  String path = "https://enablix.sharepoint.com/_api/web/GetFileByServerRelativeUrl('" 
				  			+ WebUtils.sanitizeURI(currentFileLocation) + "')/moveto(newurl='" 
				  			+ WebUtils.sanitizeURI(moveToFileLocation) + "',flags=1)"; 
		  
		  System.out.println(path);
		  RequestEntity<String> requestEntity = new RequestEntity<String>(null, headers, HttpMethod.POST, new URI(path));
		  ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);
		  return responseEntity.getBody();
	}
	
	public static void main(String[] args) throws Exception {
		try {
		SharepointOnlineTest auth = new SharepointOnlineTest();
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
		String uri = "https://enablix.sharepoint.com/_api/web/folders";
		//String folderPath = "/Shared Documents/Dikshit Test/New Folder";
		String folderPath = "/Shared Documents/Dikshit Local Env/Customer 1 Case study";
		String returnVal = auth.createFolder(uri, folderPath);
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
		
		System.out.println(returnVal);
		} catch (HttpClientErrorException | HttpServerErrorException e) {
			
			System.out.println("Error fetching folder details: " + e.getMessage());
			e.printStackTrace();
		}
		
	}
	
}
