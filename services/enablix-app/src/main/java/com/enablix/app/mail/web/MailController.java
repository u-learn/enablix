package com.enablix.app.mail.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.enablix.app.mail.ShareEmailService;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.data.segment.DataSegmentService;
import com.enablix.data.view.DataView;

@Controller
public class MailController {
	
	@Autowired
	private ShareEmailService shareEmailService;
	
	@Autowired
	private DataSegmentService dataSegmentService;
	
	@RequestMapping(method = RequestMethod.POST, value = "/sharecontent")
	public @ResponseBody Boolean sentMail(@RequestBody EmailData emailData) {
		DataView userView = dataSegmentService.getDataViewForUserId(ProcessContext.get().getUserId());
		return shareEmailService.sendEmail(emailData, userView);
	}
}
