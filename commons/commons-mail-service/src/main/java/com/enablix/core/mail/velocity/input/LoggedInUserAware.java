package com.enablix.core.mail.velocity.input;

import com.enablix.core.domain.user.User;

public interface LoggedInUserAware {

	void setLoggerInUser(User loggedInUser);
	
	User getLoggedInUser();
	
}
