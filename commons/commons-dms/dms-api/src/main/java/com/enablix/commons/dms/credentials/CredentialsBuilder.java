package com.enablix.commons.dms.credentials;

import com.enablix.core.domain.config.Configuration;

public interface CredentialsBuilder<C> {

	C buildCredentials(Configuration config);
	
}
