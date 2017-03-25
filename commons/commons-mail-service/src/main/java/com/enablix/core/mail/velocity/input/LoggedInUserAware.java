package com.enablix.core.mail.velocity.input;

import com.enablix.core.domain.security.authorization.UserProfile;

public interface LoggedInUserAware {

	void setLoggedInUser(UserProfile loggedInUser);
	
	UserProfile getLoggedInUser();
	
}
