package com.enablix.core.domain.config;

import com.enablix.core.domain.BaseDocumentEntity;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "ebx_email_conf")
public class EmailConfiguration extends BaseDocumentEntity{

	private String emailId;	
	private String tenantId;
	private String password;	
	private String smtp;	
	private String port;

	public String getTenantId() {
		return tenantId;
	};

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	};
	
	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSmtp() {
		return smtp;
	}

	public void setSmtp(String smtp) {
		this.smtp = smtp;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}
	
	
}
