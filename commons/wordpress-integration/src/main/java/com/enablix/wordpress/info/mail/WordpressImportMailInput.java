package com.enablix.wordpress.info.mail;

import java.util.List;

import com.enablix.app.content.ui.NavigableContent;
import com.enablix.core.domain.security.authorization.UserProfile;
import com.enablix.core.mail.velocity.input.BaseVelocityInput;
import com.enablix.core.mail.velocity.input.EnvPropertiesAware;
import com.enablix.core.mail.velocity.input.RecipientUserAware;

public class WordpressImportMailInput extends BaseVelocityInput implements RecipientUserAware, EnvPropertiesAware {

	private String url;
	private String recipientUserId;
	private UserProfile recipientUser;
	private List<NavigableContent> importedContent;
	
	@Override
	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String getUrl() {
		return url;
	}

	@Override
	public String getRecipientUserId() {
		return this.recipientUserId;
	}

	@Override
	public void setRecipientUser(UserProfile recipientUser) {
		this.recipientUser = recipientUser;
	}

	public List<NavigableContent> getImportedContent() {
		return importedContent;
	}

	public void setImportedContent(List<NavigableContent> importedContent) {
		this.importedContent = importedContent;
	}

	public UserProfile getRecipientUser() {
		return recipientUser;
	}

	public void setRecipientUserId(String recipientUserId) {
		this.recipientUserId = recipientUserId;
	}
	
}
