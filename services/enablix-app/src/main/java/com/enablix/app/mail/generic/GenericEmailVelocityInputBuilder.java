package com.enablix.app.mail.generic;

import com.enablix.app.content.ui.DisplayContext;
import com.enablix.app.mail.web.EmailRequest;
import com.enablix.app.mail.web.EmailRequest.Recipient;
import com.enablix.data.view.DataView;

public interface GenericEmailVelocityInputBuilder<T> {

	String mailType();

	void processInputForRecipient(Recipient recipient, T input, DataView dataView, DisplayContext ctx);

	T build(EmailRequest request, DataView dataView);
	
}
