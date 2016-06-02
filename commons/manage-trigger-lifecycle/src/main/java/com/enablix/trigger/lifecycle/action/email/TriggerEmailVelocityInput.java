
package com.enablix.trigger.lifecycle.action.email;

import java.util.Collection;

import com.enablix.app.content.ui.format.DisplayableContent;
import com.enablix.core.domain.user.User;
import com.enablix.core.mail.velocity.input.BaseVelocityInput;
import com.enablix.core.mail.velocity.input.EnvPropertiesAware;
import com.enablix.core.mail.velocity.input.LoggedInUserAware;
import com.enablix.core.mail.velocity.input.RecipientUserAware;

public class TriggerEmailVelocityInput extends BaseVelocityInput implements LoggedInUserAware, RecipientUserAware, EnvPropertiesAware {

	private String url;
	private User loggedInUser;
	private Collection<DisplayableContent> emailContent;
	private String triggerEntityTitle;
	private String recipientUserId;
	private User recipientUser;
	
	@Override
	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String getUrl() {
		return url;
	}

	@Override
	public void setLoggedInUser(User loggedInUser) {
		this.loggedInUser = loggedInUser;
	}

	@Override
	public User getLoggedInUser() {
		return loggedInUser;
	}

	public Collection<DisplayableContent> getEmailContent() {
		return emailContent;
	}

	public String getTriggerEntityTitle() {
		return triggerEntityTitle;
	}

	public void setTriggerEntityTitle(String triggerEntityTitle) {
		this.triggerEntityTitle = triggerEntityTitle;
	}

	public void setEmailContent(Collection<DisplayableContent> emailContent) {
		this.emailContent = emailContent;
	}

	@Override
	public String getRecipientUserId() {
		return recipientUserId;
	}

	@Override
	public void setRecipientUser(User recipientUser) {
		this.recipientUser = recipientUser;
	}

	public User getRecipientUser() {
		return recipientUser;
	}

	public void setRecipientUserId(String recipientUserId) {
		this.recipientUserId = recipientUserId;
	}

}
