package com.enablix.core.mail;

import org.springframework.stereotype.Component;

import com.enablix.core.mail.entities.EmailRequest;
import com.enablix.core.mail.velocity.AbstractEmailVelocityInputBuilder;
import com.enablix.data.view.DataView;

@Component
public class BasicEmailVelocityInputBuilder extends AbstractEmailVelocityInputBuilder<BasicEmailVelocityInput> {

	public static final String MAIL_TYPE_GENERAL = "general";
	
	@Override
	public String mailType() {
		return MAIL_TYPE_GENERAL;
	}

	@Override
	protected BasicEmailVelocityInput createInputInstance() {
		return new BasicEmailVelocityInput();
	}

	@Override
	protected BasicEmailVelocityInput buildInput(BasicEmailVelocityInput input, EmailRequest request,
			DataView dataView) {
		return input;
	}

}
