package com.enablix.core.mail;

import com.enablix.app.content.ui.DisplayContext;
import com.enablix.core.mail.entities.EmailRequest;
import com.enablix.core.mail.entities.EmailRequest.Recipient;
import com.enablix.data.view.DataView;

public interface GenericEmailVelocityInputBuilder<T> {

	String mailType();

	void processInputForRecipient(Recipient recipient, T input, DataView dataView, DisplayContext ctx);

	T build(EmailRequest request, DataView dataView);
	
}
