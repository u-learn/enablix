package com.enablix.ms.graph.model;

import java.util.Calendar;
import java.util.Date;

public class OAuth20Token {

	private String token_type;
	private String access_token;
	private long expires_in;
	
	private Date expiresAt;
	
	public String getToken_type() {
		return token_type;
	}
	
	public void setToken_type(String token_type) {
		this.token_type = token_type;
	}
	
	public String getAccess_token() {
		return access_token;
	}
	
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
	
	public long getExpires_in() {
		return expires_in;
	}
	
	public void setExpires_in(long expires_in) {
		
		this.expires_in = expires_in;

		Calendar now = Calendar.getInstance();
		now.add(Calendar.SECOND, (int) (expires_in - 60*5)); // 5 min margin
		this.expiresAt = now.getTime();
	}
	
	public Date expiresAt() {
		return expiresAt;
	}
	
}
