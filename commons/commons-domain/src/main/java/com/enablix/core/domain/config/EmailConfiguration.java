package com.enablix.core.domain.config;

import com.enablix.core.domain.BaseDocumentEntity;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "ebx_email_conf")
public class EmailConfiguration extends BaseDocumentEntity{

	private String emailId;	
	private String password;	
	private String smtp;	
	private String port;
	private String personalName;
	
	
	
	public EmailConfiguration(String emailId, String password, String smtp, String port, String personalName) {
		super();
		this.emailId = emailId;
		this.password = password;
		this.smtp = smtp;
		this.port = port;
		this.personalName = personalName;
	}

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
	
	
	public String getPersonalName() {
		return personalName;
	}

	public void setPersonalName(String personalName) {
		this.personalName = personalName;
	}

	@Override
	public String toString() {
		return "EmailConfiguration [emailId=" + emailId + ", smtp=" + smtp + ", port=" + port
				+ "personamName" + personalName + "]";
	}
}
