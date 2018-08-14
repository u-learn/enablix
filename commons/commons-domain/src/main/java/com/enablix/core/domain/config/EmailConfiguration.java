package com.enablix.core.domain.config;

import com.enablix.commons.util.beans.BeanUtil;
import com.enablix.core.domain.BaseDocumentEntity;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "ebx_email_conf")
public class EmailConfiguration extends BaseDocumentEntity{

	private String emailId;
	private String smtpUsername;
	private String password;	
	private String smtp;	
	private String port;
	private String personalName;
	private String replyTo;

	public EmailConfiguration(String emailId, String smtpUsername, String password, 
			String smtp, String port, String personalName, String replyTo) {
		super();
		this.emailId = emailId;
		this.smtpUsername = smtpUsername;
		this.password = password;
		this.smtp = smtp;
		this.port = port;
		this.personalName = personalName;
		this.replyTo = replyTo;
	}
	
	public EmailConfiguration() {

	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getSmtpUsername() {
		return smtpUsername;
	}

	public void setSmtpUsername(String smtpUsername) {
		this.smtpUsername = smtpUsername;
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

	public String getReplyTo() {
		return replyTo;
	}

	public void setReplyTo(String replyTo) {
		this.replyTo = replyTo;
	}

	
	@Override
	public String toString() {
		return "EmailConfiguration [emailId=" + emailId + ", smtpUsername=" + smtpUsername + ", password=" + password
				+ ", smtp=" + smtp + ", port=" + port + ", personalName=" + personalName + ", replyTo=" + replyTo + "]";
	}

	public static EmailConfiguration createCopy(EmailConfiguration config) {
		return new EmailConfiguration(config.getEmailId(), config.getSmtpUsername(), config.getPassword(), 
				config.getSmtp(), config.getPort(), config.getPersonalName(), config.getReplyTo());
	}

	public static void merge(EmailConfiguration source, EmailConfiguration target) {
		BeanUtil.copyNonNullProperties(source, target);
	}
	
}
