package com.enablix.core.mail.velocity.resolver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.commons.util.StringUtil;
import com.enablix.core.domain.security.authorization.UserProfile;
import com.enablix.core.mail.velocity.VelocityTemplateInputResolver;
import com.enablix.core.mail.velocity.input.RecipientUserAware;
import com.enablix.core.security.auth.repo.UserProfileRepository;

@Component
public class RecipientUserResolver implements VelocityTemplateInputResolver<RecipientUserAware> {

	@Autowired
	private UserProfileRepository userProfileRepo;
	
	@Override
	public void work(RecipientUserAware velocityTemplateInput) {
		String userId = velocityTemplateInput.getRecipientUserId();
		if (!StringUtil.isEmpty(userId)) {
			UserProfile recipientUserProfile = userProfileRepo.findByEmail(userId.toLowerCase());
			velocityTemplateInput.setRecipientUser(recipientUserProfile);
		}
	}

	@Override
	public boolean canHandle(Object velocityTemplateInput) {
		return velocityTemplateInput instanceof RecipientUserAware;
	}

	@Override
	public float executionOrder() {
		return VelocityResolverExecOrder.NEW_USER_EXEC_ORDER;
	}
	
}
