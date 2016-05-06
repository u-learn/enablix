package com.enablix.core.mail.velocity.input;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;

import com.enablix.app.content.ui.NavigableContent;

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
		// TODO Auto-generated method stub
		return url;
	}

		
}
