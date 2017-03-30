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
				true, siteUrl, folderUrl);
	}
	
	public String getCreateFolderJson(String folderUrl) {
		return getFormattedString(spProperties.getCreateFolderJson(), false, WebUtils.encodeURI(folderUrl));
	}
	
	public String getCreateFolderUri(String siteUrl) {
		return getFormattedString(spProperties.getCreateFolderUri(), true, siteUrl);
	}
	
	public String getPostFileUri(String siteUrl, String folderPath, String filename) {
		return getFormattedString(spProperties.getPostFileUri(), true, siteUrl, 
				WebUtils.encodeURI(folderPath), WebUtils.encodeURI(filename));
	}
	
	public String getDownloadFileUri(String siteUrl, String fileLocation) {
		return getFormattedString(spProperties.getDownloadFileUri(), true, siteUrl, WebUtils.encodeURI(fileLocation));
	}
	
	public String getMoveFileUri(String siteUrl, String currentFileLocation, String moveToFileLocation) {
		return getFormattedString(spProperties.getMoveFileUri(), true, siteUrl, 
				WebUtils.encodeURI(currentFileLocation), WebUtils.encodeURI(moveToFileLocation));
	}
	
	public String getDeleteFileUri(String siteUrl, String fileLocation) {
		return getFormattedString(spProperties.getDeleteFileUri(), true, siteUrl, WebUtils.encodeURI(fileLocation));
	}
	
	private String getFormattedString(String value, boolean sanitize, final String... args) {
		
		Map<String, String> values = new HashMap<>();
		for (int i = 0; i < args.length; i++) {
			values.put(String.valueOf(i), args[i]);
		}
		
		String str = StrSubstitutor.replace(value, values);
		return sanitize ? WebUtils.sanitizeURI(str) : str;
	}
	
	
}
