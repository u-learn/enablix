package com.enablix.core.mail.service;

import com.enablix.core.mongo.config.repo.EmailConfiguration;

public interface MailService {
	public boolean sendHtmlEmail(Object objectToBeMerged, String elementName, String templateName, String toMailAddress, String fromMailAddress, String subject,String tetantId);
}
