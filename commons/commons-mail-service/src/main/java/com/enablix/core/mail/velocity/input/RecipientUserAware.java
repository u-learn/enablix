package com.enablix.core.mail.velocity.input;

import com.enablix.core.domain.security.authorization.UserProfile;

public interface RecipientUserAware {

	String getRecipientUserId();

	void setRecipientUser(UserProfile recipientUser);
	
	UserProfile getRecipientUser();
	
}
