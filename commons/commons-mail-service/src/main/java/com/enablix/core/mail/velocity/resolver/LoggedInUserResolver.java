package com.enablix.core.mail.velocity.resolver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.domain.security.authorization.UserProfile;
import com.enablix.core.mail.velocity.VelocityTemplateInputResolver;
import com.enablix.core.mail.velocity.input.LoggedInUserAware;
import com.enablix.core.security.auth.repo.UserProfileRepository;
import com.enablix.data.view.DataView;

@Component
public class LoggedInUserResolver implements VelocityTemplateInputResolver<LoggedInUserAware> {

	@Autowired
	private UserProfileRepository userProfileRepo;
	
	@Override
	public void work(LoggedInUserAware velocityTemplateInput, DataView view) {
		String userId = ProcessContext.get().getUserId();
		
		UserProfile loggedInUserProfile = userProfileRepo.findByEmail(userId.toLowerCase());
		velocityTemplateInput.setLoggedInUser(loggedInUserProfile);
	}

	@Override
	public boolean canHandle(Object velocityTemplateInput) {
		return velocityTemplateInput instanceof LoggedInUserAware;
	}

	@Override
	public float executionOrder() {
		return VelocityResolverExecOrder.LOGGED_IN_USER_EXEC_ORDER;
	}
	
}
