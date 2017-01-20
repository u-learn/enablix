package com.enablix.core.mail.velocity.input;

import com.enablix.commons.util.id.IdentityUtil;
import com.enablix.core.ui.DisplayableContent;

public class ShareContentVelocityInput extends UserWelcomeVelocityInput implements EnvPropertiesAware {
	
	private DisplayableContent sharedContent;
	private String url;
	private String identity;
	private String emailCustomContent;
	public String getEmailCustomContent() {
		return emailCustomContent;
	}

	public void setEmailCustomContent(String emailCustomContent) {
		this.emailCustomContent = emailCustomContent;
	}

	public ShareContentVelocityInput(String newUserId, DisplayableContent sharedContent,String emailCustomContent) {
		super(newUserId);
		this.sharedContent = sharedContent;
		this.identity = IdentityUtil.generateIdentity(this);
		this.emailCustomContent=emailCustomContent;
	}

	public DisplayableContent getSharedContent() {
		return sharedContent;
	}

	public void setSharedContent(DisplayableContent sharedContent) {
		this.sharedContent = sharedContent;
	}

	@Override
	public void setUrl(String url) {
		this.url  = url;
	}

	@Override
	public String getUrl() {
		return url;
	}

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

		
}
