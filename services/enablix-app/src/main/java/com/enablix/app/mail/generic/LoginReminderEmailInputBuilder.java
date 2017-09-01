package com.enablix.app.mail.generic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.mail.web.EmailRequest;
import com.enablix.app.mail.web.EmailRequest.Recipient;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.domain.user.User;
import com.enablix.core.system.repo.UserRepository;
import com.enablix.data.view.DataView;

@Component
public class LoginReminderEmailInputBuilder extends AbstractEmailVelocityInputBuilder<LoginReminderMailInput> {

	@Autowired
	private UserRepository userRepo;
	
	@Override
	public String mailType() {
		return GenericMailConstants.MAIL_TYPE_LOGIN_REMINDER;
	}

	@Override
	protected LoginReminderMailInput buildInput(LoginReminderMailInput input, EmailRequest request,
			DataView dataView) {
		return input;
	}
	
	@Override
	public void processInputForRecipient(Recipient recipient, LoginReminderMailInput input, DataView dataView) {
		
		User user = userRepo.findByUserIdAndTenantId(recipient.getEmailId(), ProcessContext.get().getTenantId());
		if (user != null) {
			input.setPassword(user.getPassword());
		} else {
			throw new IllegalArgumentException("Invalid user id [" + recipient.getEmailId() + "]");
		}

		super.processInputForRecipient(recipient, input, dataView);
		
	}

	@Override
	protected LoginReminderMailInput createInputInstance() {
		return new LoginReminderMailInput();
	}

}