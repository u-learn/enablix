package com.enablix.core.mail.velocity.input;

import com.enablix.core.domain.user.User;

public interface RecipientUserAware {

	String getRecipientUserId();

	void setRecipientUser(User recipientUser);
	
}
