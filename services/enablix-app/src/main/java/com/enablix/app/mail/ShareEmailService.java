package com.enablix.app.mail;

import com.enablix.app.mail.web.EmailData;
import com.enablix.data.view.DataView;

public interface ShareEmailService {

	public boolean sendEmail(EmailData data, DataView view);
}
