package com.enablix.core.mail.velocity.input;

import com.enablix.commons.util.id.IdentityUtil;
import com.enablix.core.ui.DisplayableContent;

public class ShareContentVelocityInput extends UserWelcomeVelocityInput implements EnvPropertiesAware {
	
	private DisplayableContent sharedContent;
	private String url;
	private String identity;

	public ShareContentVelocityInput(String newUserId, DisplayableContent sharedContent) {
		super(newUserId);
		this.sharedContent = sharedContent;
		this.identity = IdentityUtil.generateIdentity(this);
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
