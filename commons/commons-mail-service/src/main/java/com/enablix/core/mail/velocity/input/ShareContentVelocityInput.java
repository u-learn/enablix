package com.enablix.core.mail.velocity.input;

public class ShareContentVelocityInput extends UserWelcomeVelocityInput implements EnvPropertiesAware{
	
	private Object sharedContent;
	private String url;

	public ShareContentVelocityInput(String newUserId, Object sharedContent) {
		super(newUserId);
		this.sharedContent = sharedContent;
	}

	public Object getSharedContent() {
		return sharedContent;
	}

	public void setSharedContent(Object sharedContent) {
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
