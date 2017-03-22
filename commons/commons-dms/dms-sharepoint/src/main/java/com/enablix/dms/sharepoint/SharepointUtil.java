package com.enablix.dms.sharepoint;

import java.util.HashMap;
import java.util.Map;

public class SharepointUtil {

	private static final String SHAREPOINT_COM = "sharepoint.com";
	
	private static Map<String, String> namespaces;
	
	static {
		namespaces = new HashMap<>();
		namespaces.put("S", "http://www.w3.org/2003/05/soap-envelope");
		namespaces.put("wsse", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd");
		namespaces.put("wsu", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd");
		namespaces.put("wsa", "http://www.w3.org/2005/08/addressing");
		namespaces.put("wst", "http://schemas.xmlsoap.org/ws/2005/02/trust");
	}
	
	public static String getSPEndpointAddress(String siteUrl) {
		
		String endpoint = siteUrl;
		
		if (!siteUrl.endsWith(SHAREPOINT_COM)) {
			endpoint = siteUrl.substring(0, siteUrl.indexOf(SHAREPOINT_COM) + SHAREPOINT_COM.length());
		}
		
		return endpoint;
	}
	
	public static Map<String, String> sharepointNamespaces() {
		return namespaces;
	}
	
	public static String getFileLocationRelativeToBaseFolder(String fileLocation, String baseFolder) {
		
		String relativeLocation = fileLocation;
		
		if (fileLocation.startsWith(baseFolder)) {
			relativeLocation = fileLocation.substring(baseFolder.length());
		}
		
		return relativeLocation;
	}

	public static String createFileLocationWithBaseFolder(String fileLocation, String baseFolder) {
		return baseFolder + "/" + fileLocation;
	}

	
}
