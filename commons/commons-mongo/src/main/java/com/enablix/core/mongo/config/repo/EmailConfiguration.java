package com.enablix.core.mongo.config.repo;

import com.enablix.core.domain.BaseDocumentEntity;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "ebxEmailConf")
public class EmailConfiguration extends BaseDocumentEntity{

	private String emailId;
	
	private String tetantId;	

	private String password;
	
	private String smtp;
	
	private String port;

	public String getTetantId() {
		return tetantId;
	};

	public void setTetantId(String tetantId) {
		this.tetantId = tetantId;
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
