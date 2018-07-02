package com.enablix.ms.graph.model;

import java.util.Date;

public class UploadSession {

	private String uploadUrl;
	
	private Date expirationDateTime;

	public String getUploadUrl() {
		return uploadUrl;
	}

	public void setUploadUrl(String uploadUrl) {
		this.uploadUrl = uploadUrl;
	}

	public Date getExpirationDateTime() {
		return expirationDateTime;
	}

	public void setExpirationDateTime(Date expirationDateTime) {
		this.expirationDateTime = expirationDateTime;
	}
	
}
