package com.enablix.core.mail.velocity.resolver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.commons.util.StringUtil;
import com.enablix.core.domain.user.User;
import com.enablix.core.mail.velocity.VelocityTemplateInputResolver;
import com.enablix.core.mail.velocity.input.RecipientUserAware;
import com.enablix.core.system.repo.UserRepository;

@Component
public class RecipientUserResolver implements VelocityTemplateInputResolver<RecipientUserAware> {

	@Autowired
	private UserRepository userRepo;
	
	@Override
	public void work(RecipientUserAware velocityTemplateInput) {
		String userId = velocityTemplateInput.getRecipientUserId();
		if (!StringUtil.isEmpty(userId)) {
			User recipientUser = userRepo.findByUserId(userId.toLowerCase());
			velocityTemplateInput.setRecipientUser(recipientUser);
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
