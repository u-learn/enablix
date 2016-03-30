package com.enablix.core.mail.velocity.resolver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.domain.user.User;
import com.enablix.core.mail.velocity.VelocityTemplateInputResolver;
import com.enablix.core.mail.velocity.input.LoggedInUserAware;
import com.enablix.core.mail.velocity.input.UserWelcomeVelocityInput;
import com.enablix.core.system.repo.UserRepository;

@Component
public class NewUserResolver implements VelocityTemplateInputResolver<UserWelcomeVelocityInput> {

	@Autowired
	private UserRepository userRepo;
	
	@Override
	public void work(UserWelcomeVelocityInput velocityTemplateInput) {
		String userId = velocityTemplateInput.getNewUserId();
		User newUser = userRepo.findByUserId(userId);
		velocityTemplateInput.setNewCreatedUser(newUser);
	}

	@Override
	public boolean canHandle(Object velocityTemplateInput) {
		return velocityTemplateInput instanceof UserWelcomeVelocityInput;
	}

	
	
}
