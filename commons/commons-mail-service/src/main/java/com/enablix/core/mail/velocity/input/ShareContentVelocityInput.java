package com.enablix.core.mail.velocity.input;

import java.util.HashMap;
import java.util.List;

import com.enablix.app.content.ui.NavigableContent;

public class ShareContentVelocityInput extends UserWelcomeVelocityInput{
	
	private Object sharedContent;
	
	public ShareContentVelocityInput(String newUserId, Object sharedContent) {
		super(newUserId);
		this.sharedContent = sharedContent;
		// TODO Auto-generated constructor stub
	}

	public Object getSharedContent() {
		return sharedContent;
	}

	public void setSharedContent(Object sharedContent) {
		this.sharedContent = sharedContent;
	}

	

		
}
