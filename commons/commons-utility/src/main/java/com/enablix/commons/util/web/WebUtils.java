package com.enablix.commons.util.web;

import java.io.BufferedReader;
import java.io.IOException;

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
	
}
