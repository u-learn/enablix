package com.enablix.core.mail.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.enablix.core.domain.config.EmailConfiguration;
import com.enablix.core.domain.config.SMTPConfiguration;
import com.enablix.core.mail.service.MailService;


@RestController
public class MailConfController {
	
	@Autowired
	MailService mailService;
	
	/*@RequestMapping(method = RequestMethod.GET, value = "/sentMail", produces = "application/json")
	public Boolean sentMail() {
		return mailService.sendHtmlEmail(new Integer(0), "User", "welcome.vm", "john.wakad@gmail.com", null, null, "test1");
	}*/
	
	@RequestMapping(method = RequestMethod.GET, value = "/getsmtpconfig", produces = "application/json")
	public SMTPConfiguration getSmtpConfig(String domainName) {
		return mailService.getSMTPConfig(domainName);
	};
	
	@RequestMapping(method = RequestMethod.DELETE, value = "/deleteemailconfiguration", produces = "application/json")
	public Boolean deleteEmailConfiguration(String tetantId) {
		return mailService.deleteEmailConfiguration(tetantId);
	};
	
	@RequestMapping(method = RequestMethod.POST, value = "/addemailconfiguration", produces = "application/json")
	public EmailConfiguration addEmailConfiguration(EmailConfiguration emailConfiguration) {
		return mailService.addEmailConfiguration(emailConfiguration);
	};
	
		
}
