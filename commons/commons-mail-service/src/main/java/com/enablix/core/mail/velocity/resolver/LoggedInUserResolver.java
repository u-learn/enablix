package com.enablix.core.mail.velocity.resolver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.domain.user.User;
import com.enablix.core.mail.velocity.VelocityTemplateInputResolver;
import com.enablix.core.mail.velocity.input.LoggedInUserAware;
import com.enablix.core.system.repo.UserRepository;

@Component
public class LoggedInUserResolver implements VelocityTemplateInputResolver<LoggedInUserAware> {

	@Autowired
	private UserRepository userRepo;
	
	@Override
	public void work(LoggedInUserAware velocityTemplateInput) {
		String userId = ProcessContext.get().getUserId();
		User loggedInUser = userRepo.findByUserId(userId.toLowerCase());
		velocityTemplateInput.setLoggedInUser(loggedInUser);
	}

	@Override
	public boolean canHandle(Object velocityTemplateInput) {
		return velocityTemplateInput instanceof LoggedInUserAware;
	}

	
	
}
