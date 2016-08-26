package com.enablix.app.content.ui.format;

import com.enablix.app.content.share.SharedContentUrlCreator;
import com.enablix.commons.util.EnvPropertiesUtil;
import com.enablix.commons.util.TextLinkifier.LinkDecorator;

public class EmailExtLinkDecorator implements LinkDecorator {

	private SharedContentUrlCreator urlCreator;
	private String sharedWithEmail;
	private String contentIdentity;
	
	public EmailExtLinkDecorator(SharedContentUrlCreator urlCreator, String sharedWithEmail, 
			String contentIdentity) {
		super();
		this.urlCreator = urlCreator;
		this.sharedWithEmail = sharedWithEmail;
		this.contentIdentity = contentIdentity;
	}

	@Override
	public String getHref(String url, String contentItemQId) {
		String sharedUrl = urlCreator.createShareableUrl(getExtLinkUrl(url, contentItemQId), sharedWithEmail, true);
		return EnvPropertiesUtil.getProperties().getServerUrl() + sharedUrl;
	}

	private String getExtLinkUrl(String url, String contentItemQId) {
		return "/xlink?u=" + url + "&cId=" + contentIdentity + "&cQId=" + contentItemQId + "&atChannel=EMAIL";
	}

	@Override
	public String getLinkText(String url) {
		return "Click Here";
	}

}
