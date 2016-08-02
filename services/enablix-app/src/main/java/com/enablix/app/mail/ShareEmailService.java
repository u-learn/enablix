package com.enablix.app.mail;

import com.enablix.app.mail.web.EmailData;

public interface ShareEmailService {

	public boolean sendEmail(EmailData data);
	
}
