package com.enablix.trigger.lifecycle.action.email;

import java.util.Collection;
import java.util.List;

import com.enablix.app.content.ui.format.DisplayableContent;
import com.enablix.core.api.ContentDataRecord;
import com.enablix.core.domain.user.User;
import com.enablix.core.mail.velocity.input.BaseVelocityInput;
import com.enablix.core.mail.velocity.input.EnvPropertiesAware;
import com.enablix.core.mail.velocity.input.LoggedInUserAware;

public class TriggerEmailVelocityInput extends BaseVelocityInput implements LoggedInUserAware, EnvPropertiesAware {

	private String url;
	private User loggedInUser;
	private Collection<DisplayableContent> emailContent;
	
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

	public void setEmailContent(List<DisplayableContent> displayableEmailContent) {
		this.emailContent = displayableEmailContent;
	}

}
