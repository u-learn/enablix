package com.enablix.app.mail;

public interface SharedContentUrlCreator {

	String createShareableUrl(String actualUrl, String sharedWithEmail, boolean allowUnAuthAccess);
	
}
