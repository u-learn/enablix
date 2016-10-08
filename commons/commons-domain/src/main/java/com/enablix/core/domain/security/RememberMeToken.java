package com.enablix.core.domain.security;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;

@Document(collection = "ebx_remember_me_token")
public class RememberMeToken {

	@Id
	private String id;
	
	private PersistentRememberMeToken token;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public PersistentRememberMeToken getToken() {
		return token;
	}

	public void setToken(PersistentRememberMeToken token) {
		this.token = token;
	}

}
