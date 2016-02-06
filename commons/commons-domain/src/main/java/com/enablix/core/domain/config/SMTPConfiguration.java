package com.enablix.core.domain.config;
import com.enablix.core.domain.BaseDocumentEntity;

import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "ebxSmtpConf")
public class SMTPConfiguration extends BaseDocumentEntity{
		
	private String domainName;
	
	private String smtp;
	
	private String port;

	public String getDomianName() {
		return domainName;
	}

	public void setDomianName(String domianName) {
		this.domainName = domianName;
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
