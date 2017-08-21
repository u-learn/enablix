package com.enablix.app.mail.generic;

import org.springframework.stereotype.Component;

import com.enablix.app.mail.web.EmailRequest;
import com.enablix.data.view.DataView;

@Component
public class BasicEmailVelocityInputBuilder extends AbstractEmailVelocityInputBuilder<BasicEmailVelocityInput> {

	@Override
	public String mailType() {
		return GenericMailConstants.MAIL_TYPE_GENERAL;
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
