package com.enablix.commons.dms.credentials;

import org.springframework.stereotype.Component;

import com.enablix.commons.dms.DMSConstants;
import com.enablix.core.domain.config.Configuration;

@Component
public class UsernamePasswordCredentialsBuilder implements CredentialsBuilder<UsernamePasswordCredentials> {

	public UsernamePasswordCredentials buildCredentials(Configuration config) {
		
		String userName = config.getStringValue(DMSConstants.CFG_USERNAME_KEY);
		String password = config.getStringValue(DMSConstants.CFG_PASSWORD_KEY);
		
		return new UsernamePasswordCredentials(userName, password);
	}
	
}
