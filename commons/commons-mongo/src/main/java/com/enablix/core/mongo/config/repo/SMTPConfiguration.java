package com.enablix.core.mongo.config.repo;
import java.util.Map;

import com.enablix.core.domain.BaseDocumentEntity;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "ebx_smtp_conf")
public class SMTPConfiguration extends BaseDocumentEntity{

	private String Identity;
	
	private String domianName;
	
	private String smtp;
	
	private String port;

	public String getIdentity() {
		return Identity;
	}

	public void setIdentity(String identity) {
		Identity = identity;
	}

	public String getDomianName() {
		return domianName;
	}

	public void setDomianName(String domianName) {
		this.domianName = domianName;
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
