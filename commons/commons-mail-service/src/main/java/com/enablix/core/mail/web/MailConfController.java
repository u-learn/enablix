package com.enablix.core.mail.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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


	
	@RequestMapping(method = RequestMethod.GET, value = "/getsmtpconfig/{domainName}", produces = "application/json")
	public SMTPConfiguration getSmtpConfig(@PathVariable String domainName) {
		return mailService.getSMTPConfig(domainName);
	};
	
	@RequestMapping(method = RequestMethod.POST, value = "/deleteemailconfiguration", produces = "application/json")
	public Boolean deleteEmailConfiguration(@RequestBody EmailConfiguration emailConfiguration) {
		return mailService.deleteEmailConfiguration(emailConfiguration);
	};
	
	@RequestMapping(method = RequestMethod.POST, value = "/addemailconfiguration", produces = "application/json")
	public EmailConfiguration addEmailConfiguration(@RequestBody EmailConfiguration emailConfiguration) {
		return mailService.addEmailConfiguration(emailConfiguration);
	};
	@RequestMapping(method = RequestMethod.GET, value = "/getemailconfiguration", produces = "application/json")
	public EmailConfiguration getemailconfiguration() {
		return mailService.getEmailConfiguration();
	};
	@RequestMapping(method = RequestMethod.POST, value = "/sendmail", produces = "application/json")
	public Boolean sentMail(@RequestBody EmailData emailData) {
		return mailService.sendHtmlEmail(emailData.getTemplateObject(),emailData.getEmailid(),emailData.getScenario());
	};		
}
