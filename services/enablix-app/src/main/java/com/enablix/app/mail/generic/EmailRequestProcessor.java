package com.enablix.app.mail.generic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.mail.web.EmailRequest;
import com.enablix.app.mail.web.EmailRequest.Recipient;
import com.enablix.core.mail.service.MailService;
import com.enablix.data.view.DataView;
import com.enablix.services.util.DataViewUtil;

@Component
public class EmailRequestProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmailRequestProcessor.class);
	
	@Autowired
	private GenericEmailVelocityInputBuilderFactory builderFactory;
	
	@Autowired
	private MailService mailService;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void sendEmail(String mailType, EmailRequest request) {
		
		GenericEmailVelocityInputBuilder builder = builderFactory.getBuilder(mailType);
		if (builder == null) {
			LOGGER.error("No builder found for mail: {}", mailType);
			throw new IllegalStateException("No builder found for mail: " + mailType);
		}
		
		DataView dataView = DataViewUtil.allDataView();
		Object velocityInput = builder.build(request, dataView);
		
		for (Recipient recipient : request.getRecipients()) {
			builder.processInputForRecipient(recipient, velocityInput, dataView);
			mailService.sendHtmlEmail(velocityInput, recipient.getEmailId(), request.getMailTemplateId());
		}
		
	}
	
}
