package com.enablix.core.mail.entities;

public class MailEvent {

	private Object mailTemplateInput;
	
	private String toEmailId;
	
	private String mailScenario;

	@SuppressWarnings("unused")
	private MailEvent() {
		// for ORM
	}
	
	public MailEvent(Object mailTemplateInput, String toEmailId, String mailScenario) {
		super();
		this.mailTemplateInput = mailTemplateInput;
		this.toEmailId = toEmailId;
		this.mailScenario = mailScenario;
	}

	public Object getMailTemplateInput() {
		return mailTemplateInput;
	}

	public void setMailTemplateInput(Object mailTemplateInput) {
		this.mailTemplateInput = mailTemplateInput;
	}

	public String getToEmailId() {
		return toEmailId;
	}

	public void setToEmailId(String toEmailId) {
		this.toEmailId = toEmailId;
	}

	public String getMailScenario() {
		return mailScenario;
	}

	public void setMailScenario(String mailScenario) {
		this.mailScenario = mailScenario;
	}
	
	
}
