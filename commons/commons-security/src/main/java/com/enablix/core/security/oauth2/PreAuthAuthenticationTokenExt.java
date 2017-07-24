package com.enablix.core.security.oauth2;

import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

public class PreAuthAuthenticationTokenExt extends PreAuthenticatedAuthenticationToken {

	private static final long serialVersionUID = 1L;

	public PreAuthAuthenticationTokenExt() {
		super(null, null);
	}

}
