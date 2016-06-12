package com.enablix.site.contactus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.enablix.commons.util.concurrent.Events;
import com.enablix.core.mail.service.MailService;
import com.enablix.core.mq.Event;
import com.enablix.core.mq.EventSubscription;
import com.enablix.core.mq.util.EventUtil;
import com.enablix.core.system.repo.ContactUsRepository;
import com.enablix.website.ContactUsRequest;

@Component
public class ContactUsManagerImpl implements ContactUsManager {

	@Value("${contactus.notification.mail.address}")
	private String contactUsToEmail;
	
	@Autowired
	private ContactUsRepository contactUsRepo;
	
	@Autowired
	private MailService mailService;
	
	
	@Override
	public void captureContactUsRequest(ContactUsRequest contactUs) {
		contactUsRepo.save(contactUs);
		EventUtil.publishEvent(new Event<ContactUsRequest>(Events.NEW_CONTACT_US_REQUEST, contactUs));
	}
	
	@EventSubscription(eventName = {Events.NEW_CONTACT_US_REQUEST})
	public void notifyContactUsRequest(ContactUsRequest contactUs) {
		mailService.sendHtmlEmail(contactUs, contactUsToEmail, "contactus");
	}

}
