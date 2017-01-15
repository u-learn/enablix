package com.enablix.core.mail.service;

import com.enablix.core.domain.config.EmailConfiguration;
import com.enablix.core.domain.config.SMTPConfiguration;
import com.enablix.core.domain.config.TemplateConfiguration;
import com.enablix.core.mail.entities.ShareEmailClientDtls;


public interface MailService {
	public boolean sendHtmlEmail(Object objectTobeMerged, String emailid,String scenario);
	public ShareEmailClientDtls getHtmlEmail(Object objectTobeMerged, String emailid,String scenario);
	public EmailConfiguration addEmailConfiguration (EmailConfiguration emailConfiguration);
	public SMTPConfiguration getSMTPConfig(String domainName);
	public Boolean deleteEmailConfiguration(EmailConfiguration emailConfiguration);
	public EmailConfiguration getEmailConfiguration();
	TemplateConfiguration getTemplateConfiguration(String scenario);
	TemplateConfiguration addTemplateConfiguration(TemplateConfiguration templateConfiguration);
	Boolean deleteTemplateConfiguration(TemplateConfiguration templateConfiguration);
	boolean sendHtmlEmail(Object objectTobeMerged, String emailid, String scenario, String bodyTemplateName,
			String subjectTemplateName);
	ShareEmailClientDtls getHtmlEmail(Object objectTobeMerged, String emailid, String scenario, String bodyTemplateName,
			String subjectTemplateName);
	
}
