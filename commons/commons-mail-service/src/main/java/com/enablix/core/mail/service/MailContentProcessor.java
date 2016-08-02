package com.enablix.core.mail.service;

import java.util.List;

public interface MailContentProcessor {

	public void preSend(Object mailContent);
	
	public void postSend(Object mailContent);
	
	public List<String> interestedInScenarios();
	
	
}
