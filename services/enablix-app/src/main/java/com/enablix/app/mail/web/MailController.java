package com.enablix.app.mail.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.enablix.app.mail.ShareEmailService;
import com.enablix.app.mail.generic.EmailRequestProcessor;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.data.segment.DataSegmentService;
import com.enablix.data.view.DataView;

@RestController
@RequestMapping("email")
public class MailController {
	
	@Autowired
	private ShareEmailService shareEmailService;
	
	@Autowired
	private DataSegmentService dataSegmentService;
	
	@Autowired
	private EmailRequestProcessor emailRequestProcessor;
	
	@RequestMapping(method = RequestMethod.POST, value = "/sharecontent")
	public @ResponseBody Boolean sendShareContentMail(@RequestBody EmailData emailData) {
		DataView userView = dataSegmentService.getDataViewForUserId(ProcessContext.get().getUserId());
		return shareEmailService.sendEmail(emailData, userView);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/t/{mailtype}/")
	public @ResponseBody Boolean sendMail(@PathVariable String mailtype, @RequestBody EmailRequest emailRequest) {
		emailRequestProcessor.sendEmail(mailtype, emailRequest);
		return true;
	}
	
}
