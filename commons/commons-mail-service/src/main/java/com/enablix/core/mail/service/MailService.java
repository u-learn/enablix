package com.enablix.core.mail.service;

import com.enablix.core.domain.config.EmailConfiguration;


public interface MailService {
	public boolean sendHtmlEmail(Object objectToBeMerged, String elementName, String templateName, String toMailAddress, String fromMailAddress, String subject,String tetantId);
	public EmailConfiguration addEmailConfiguration (EmailConfiguration emailConfiguration);
}
