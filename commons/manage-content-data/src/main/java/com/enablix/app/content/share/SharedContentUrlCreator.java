package com.enablix.app.content.share;

public interface SharedContentUrlCreator {

	String createShareableUrl(String actualUrl, String sharedWithEmail, boolean allowUnAuthAccess);
	
}
