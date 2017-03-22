package com.enablix.dms.sharepoint;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.text.StrSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.commons.util.web.WebUtils;

@Component
public class SharepointAPI {

	@Autowired
	private SharepointProperties spProperties;
	
	public String getGetFolderByServerRelativePathUrl(String siteUrl, String folderUrl) {
		return getFormattedString(spProperties.getGetFolderByServerRelativePathUrl(), 
				siteUrl, WebUtils.sanitizeURI(folderUrl));
	}
	
	public String getCreateFolderJson(String folderUrl) {
		return getFormattedString(spProperties.getCreateFolderJson(), folderUrl);
	}
	
	public String getCreateFolderUri(String siteUrl) {
		return getFormattedString(spProperties.getCreateFolderUri(), siteUrl);
	}
	
	public String getPostFileUri(String siteUrl, String folderPath, String filename) {
		return getFormattedString(spProperties.getPostFileUri(), siteUrl, 
					WebUtils.sanitizeURI(folderPath), WebUtils.sanitizeURI(filename));
	}
	
	public String getDownloadFileUri(String siteUrl, String fileLocation) {
		return getFormattedString(spProperties.getDownloadFileUri(), siteUrl, WebUtils.sanitizeURI(fileLocation));
	}
	
	public String getMoveFileUri(String siteUrl, String currentFileLocation, String moveToFileLocation) {
		return getFormattedString(spProperties.getMoveFileUri(), siteUrl, 
				WebUtils.sanitizeURI(currentFileLocation), WebUtils.sanitizeURI(moveToFileLocation));
	}
	
	public String getDeleteFileUri(String siteUrl, String fileLocation) {
		return getFormattedString(spProperties.getDeleteFileUri(), siteUrl, WebUtils.sanitizeURI(fileLocation));
	}
	
	private String getFormattedString(String value, final String... args) {
		
		Map<String, String> values = new HashMap<>();
		for (int i = 0; i < args.length; i++) {
			values.put(String.valueOf(i), args[i]);
		}
		
		return StrSubstitutor.replace(value, values);
	}
	
	
}
