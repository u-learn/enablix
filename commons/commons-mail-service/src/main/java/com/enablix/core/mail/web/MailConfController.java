package com.enablix.core.mail.web;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enablix.core.mail.service.MailService;
import com.enablix.core.mongo.config.repo.EmailConfigRepo;
import com.enablix.core.mongo.config.repo.EmailConfiguration;

@RestController
public class MailConfController {
	
	@Autowired
	MailService mailService;
	
	
	@RequestMapping("/sentMail")
	public Boolean sentMail() {		
		
		mailService.sendHtmlEmail(new Integer(0), "welcome", "welcome.vm", "gjadhao089@gmail.com", "jadhaoganesh@rocketmail.com", "TESTING MAIL","test1");
		
		return true;
	}
	
}
