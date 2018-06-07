package com.enablix.app.content.ui.format;

import com.enablix.app.content.share.SharedContentUrlCreator;
import com.enablix.commons.util.EnvPropertiesUtil;
import com.enablix.commons.util.TextLinkifier.LinkDecorator;
import com.enablix.core.domain.activity.ActivityChannel.Channel;

public class ExtLinkDecorator implements LinkDecorator {

	private SharedContentUrlCreator urlCreator;
	private String sharedWithEmail;
	private String contentIdentity;
	private Channel channel;
	
	public ExtLinkDecorator(SharedContentUrlCreator urlCreator, String sharedWithEmail, 
			String contentIdentity, Channel channel) {
		super();
		this.urlCreator = urlCreator;
		this.sharedWithEmail = sharedWithEmail;
		this.contentIdentity = contentIdentity;
		this.channel = channel;
	}

	@Override
	public String getHref(String url, String contentItemQId) {
		String sharedUrl = urlCreator.createShareableUrl(getExtLinkUrl(url, contentItemQId), sharedWithEmail, true);
		return EnvPropertiesUtil.getSubdomainSpecificServerUrl() + sharedUrl;
	}

	private String getExtLinkUrl(String url, String contentItemQId) {
		return "/xlink?u=" + url + "&cId=" + contentIdentity + "&cQId=" + contentItemQId + "&atChannel=" + channel.name();
	}

	@Override
	public String getLinkText(String url) {
		return "Click Here";
	}

}
