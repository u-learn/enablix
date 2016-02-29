package com.enablix.core.mail.web;

public class EmailData {
	
	private String scenario;
	
	private String emailid;
	
	private Object templateObject;

	public String getScenario() {
		return scenario;
	}

	public void setScenario(String scenario) {
		this.scenario = scenario;
	}

	public String getEmailid() {
		return emailid;
	}

	public void setEmailid(String emailid) {
		this.emailid = emailid;
	}

	public Object getTemplateObject() {
		return templateObject;
	}

	public void setTemplateObject(Object templateObject) {
		this.templateObject = templateObject;
	}
	
	
}
