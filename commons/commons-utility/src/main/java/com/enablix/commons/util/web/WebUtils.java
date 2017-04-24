package com.enablix.commons.util.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebUtils {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(WebUtils.class);
	
	private static final String TENANT_ID_URL_PREFIX = "t/";
	private static final int TENANT_ID_URL_PREFIX_OFFSET = TENANT_ID_URL_PREFIX.length();
	
	private static final String CLIENT_ID_URL_PREFIX = "c/";
	private static final int CLIENT_ID_URL_PREFIX_OFFSET = CLIENT_ID_URL_PREFIX.length();
	
	public static String removeTenantInfo(String resourcePath) {
		
		if (resourcePath.startsWith(TENANT_ID_URL_PREFIX)) {
			
			resourcePath = resourcePath.substring(resourcePath.indexOf("/", TENANT_ID_URL_PREFIX_OFFSET) + 1);
			
			if (resourcePath.startsWith(CLIENT_ID_URL_PREFIX)) {
				resourcePath = resourcePath.substring(resourcePath.indexOf("/", CLIENT_ID_URL_PREFIX_OFFSET) + 1);
			}
		}
		
		return resourcePath;
	}
	
	public static TenantInfo getTenantInfoFromUrl(HttpServletRequest request) {
		
		TenantInfo tenantInfo = null;
		
		String requestURI = request.getRequestURI();
		requestURI = removeLeadingSlash(requestURI);

		if (requestURI.startsWith(TENANT_ID_URL_PREFIX)) {
		
			tenantInfo = new TenantInfo();
			
			int indexOfNextSlash = requestURI.indexOf("/", TENANT_ID_URL_PREFIX_OFFSET);
			String tenantId = requestURI.substring(TENANT_ID_URL_PREFIX_OFFSET, indexOfNextSlash);
			tenantInfo.setTenantId(tenantId);
			
			requestURI = requestURI.substring(indexOfNextSlash + 1);
			
			if (requestURI.startsWith(CLIENT_ID_URL_PREFIX)) {
				
				indexOfNextSlash = requestURI.indexOf("/", CLIENT_ID_URL_PREFIX_OFFSET);
				String clientId = requestURI.substring(TENANT_ID_URL_PREFIX_OFFSET, indexOfNextSlash);
				tenantInfo.setClientId(clientId);
			}
		}

		return tenantInfo;
	}
	
	private static String removeLeadingSlash(String path) {
		
		int slashCnt = 0;
		
		for (int i = 0; i < path.length(); i++) {
			if (path.charAt(i) != '/') {
				break;
			}
			slashCnt++;
		}
		
		if (slashCnt > 0) {
			path = path.substring(slashCnt);
		}
		
		return path;
	}

	public static String getPostData(HttpServletRequest request) {

		StringBuilder sb = new StringBuilder();
	    
		try {
	        BufferedReader reader = request.getReader();

	        String line;
	        do {
	            line = reader.readLine();
	            sb.append(line).append("\n");
	        } while (line != null);

		} catch(IOException e) {
	        LOGGER.warn("getPostData couldn't. get the post data", e);  // This has happened if the request's reader is closed    
	    }

	    return sb.toString();
	}
	
	public static String sanitizeURI(String uri) {
		return uri.replace(" ", "%20");
	}
	
	public static String encodeURI(String param) {
		
		String encStr = param;
		
		try {
			encStr = URLEncoder.encode(param, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			LOGGER.error("error in URL encoding", e);
			
		}
		
		encStr = encStr.replaceAll("%2F", "/").replaceAll("\\+", "%20");
		return encStr;
	}
	
	public static void main(String[] args) {
		String url = "Marketplace: Lending-{White}-(Paper)-[FINAL].pdf";
		System.out.println(encodeURI(url));
	}
	
}
