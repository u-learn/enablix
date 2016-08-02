package com.enablix.app.mail.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.enablix.app.mail.ShareEmailService;

@Controller
public class MailController {
	
	@Autowired
	private ShareEmailService shareEmailService;
	
	@RequestMapping(method = RequestMethod.POST, value = "/sharecontent")
	public @ResponseBody Boolean sentMail(@RequestBody EmailData emailData) {
		return shareEmailService.sendEmail(emailData);
	}
	
}
