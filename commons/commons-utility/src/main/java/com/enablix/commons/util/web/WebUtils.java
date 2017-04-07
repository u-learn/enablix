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
