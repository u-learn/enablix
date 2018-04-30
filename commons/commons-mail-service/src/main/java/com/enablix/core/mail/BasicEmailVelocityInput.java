package com.enablix.core.mail;

import java.util.Map;

import com.enablix.core.domain.security.authorization.UserProfile;
import com.enablix.core.mail.velocity.input.BaseVelocityInput;
import com.enablix.core.mail.velocity.input.EnvPropertiesAware;
import com.enablix.core.mail.velocity.input.RecipientUserAware;

public class BasicEmailVelocityInput extends BaseVelocityInput implements RecipientUserAware, EnvPropertiesAware {

	private String recipientUserId;
	private UserProfile recipientUser;
	private String url;
	private Map<String, Object> inputData;

	public BasicEmailVelocityInput() {
		super();
	}

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
		return recipientUserId;
	}

	@Override
	public void setRecipientUser(UserProfile recipientUser) {
		this.recipientUser = recipientUser;
	}

	public UserProfile getRecipientUser() {
		return recipientUser;
	}

	public void setRecipientUserId(String recipientUserId) {
		this.recipientUserId = recipientUserId;
	}

	public Map<String, Object> getInputData() {
		return inputData;
	}

	public void setInputData(Map<String, Object> inputData) {
		this.inputData = inputData;
	}

}