package com.enablix.core.mail.velocity.input;

import com.enablix.app.content.ui.format.DisplayableContent;

public class ShareContentVelocityInput extends UserWelcomeVelocityInput implements EnvPropertiesAware {
	
	private DisplayableContent sharedContent;
	private String url;

	public ShareContentVelocityInput(String newUserId, DisplayableContent sharedContent) {
		super(newUserId);
		this.sharedContent = sharedContent;
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

		
}
