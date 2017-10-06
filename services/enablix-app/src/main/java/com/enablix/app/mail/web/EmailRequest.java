package com.enablix.app.mail.web;

import java.util.List;
import java.util.Map;

import com.enablix.core.domain.security.authorization.UserProfile;
import com.enablix.core.mongo.search.service.SearchRequest;

public class EmailRequest {

	private String mailTemplateId;
	
	private List<Recipient> recipients;
	
	private Map<String, Object> inputData;
	
	private SearchRequest dataFilter;
	
	public String getMailTemplateId() {
		return mailTemplateId;
	}

	public void setMailTemplateId(String mailTemplateId) {
		this.mailTemplateId = mailTemplateId;
	}

	public List<Recipient> getRecipients() {
		return recipients;
	}

	public void setRecipients(List<Recipient> recipients) {
		this.recipients = recipients;
	}

	public Map<String, Object> getInputData() {
		return inputData;
	}

	public void setInputData(Map<String, Object> inputData) {
		this.inputData = inputData;
	}

	public SearchRequest getDataFilter() {
		return dataFilter;
	}

	public void setDataFilter(SearchRequest dataFilter) {
		this.dataFilter = dataFilter;
	}

	
	public static class Recipient {
		
		private String emailId;
		
		private String name;
		
		public Recipient() {
			
		}
		
		public Recipient(UserProfile user) {
			this.emailId = user.getEmail();
			this.name = user.getName();
		}

		public String getEmailId() {
			return emailId;
		}

		public void setEmailId(String emailId) {
			this.emailId = emailId;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
		
	}
	
}
