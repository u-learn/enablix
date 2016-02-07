package com.enablix.core.mail.service;

import com.enablix.core.domain.config.EmailConfiguration;
import com.enablix.core.domain.config.SMTPConfiguration;


public interface MailService {
	public boolean sendHtmlEmail(Object objectToBeMerged, String elementName, String templateName, String toMailAddress, String fromMailAddress, String subject,String tetantId);
	public EmailConfiguration addEmailConfiguration (EmailConfiguration emailConfiguration);
	public SMTPConfiguration getSMTPConfig(String domainName);
	public Boolean deleteEmailConfiguration(String tetantId);
	public EmailConfiguration getEmailConfiguration(String tetantId);
	
}
