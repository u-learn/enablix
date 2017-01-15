package com.enablix.app.mail;

import com.enablix.app.mail.web.EmailData;
import com.enablix.core.mail.entities.ShareEmailClientDtls;

public interface ShareEmailService {

	public boolean sendEmail(EmailData data);
	public ShareEmailClientDtls getEmailContent(String containerQID,String contentID);
}
