package com.enablix.core.security;

import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.domain.activity.ActivityAudit;
import com.enablix.core.domain.activity.ActivityChannel;
import com.enablix.core.domain.activity.ActivityChannel.Channel;
import com.enablix.core.domain.activity.RegisteredActor;
import com.enablix.core.domain.activity.UserAccountActivity;
import com.enablix.core.domain.activity.UserAccountActivity.AccountActivityType;
import com.enablix.core.domain.security.authorization.UserProfile;
import com.enablix.core.domain.tenant.Tenant;
import com.enablix.core.domain.user.User;
import com.enablix.core.security.auth.repo.UserProfileRepository;
import com.enablix.core.security.service.EnablixUserService.LoggedInUser;
import com.enablix.core.system.repo.TenantRepository;
import com.enablix.services.util.ActivityLogger;

@Component
public class LoginListener implements ApplicationListener<AuthenticationSuccessEvent> {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoginListener.class);

	@Autowired
	private TenantRepository tenantRepo;

	@Autowired
	private UserProfileRepository userProfileRepo;

	@Override
	public void onApplicationEvent(AuthenticationSuccessEvent event) {

		UserDetails ud = (UserDetails) event.getAuthentication().getPrincipal();

		LOGGER.info("User {} logged-in successfully", ud.getUsername());

		ActivityAudit userLogin = new ActivityAudit();

		UserAccountActivity userLoginActvy = new UserAccountActivity(AccountActivityType.LOGIN);
		userLogin.setActivity(userLoginActvy);

		userLogin.setActivityTime(Calendar.getInstance().getTime());

		userLogin.setChannel(new ActivityChannel(Channel.WEB));

		if (ud instanceof LoggedInUser) {

			User user = ((LoggedInUser) ud).getUser();
			Tenant tenant = tenantRepo.findByTenantId(user.getTenantId());

			String templateId = tenant == null ? "" : tenant.getDefaultTemplateId();
			
			try {
				
				ProcessContext.initialize(user.getUserId(), user.getUserId(), user.getTenantId(), templateId, null);

				UserProfile userProfile = userProfileRepo.findByEmail(user.getUserId());
				RegisteredActor actor = new RegisteredActor(ud.getUsername(), userProfile.getName());
				userLogin.setActor(actor);

				ProcessContext.clear();

				// set up process context to fetch user roles from tenant specific database
				ProcessContext.initialize(user.getUserId(), userProfile.getName(), user.getTenantId(), templateId, null);

				ActivityLogger.auditActivity(userLogin);

			} finally {
				ProcessContext.clear();
			}

		}
	}

}
