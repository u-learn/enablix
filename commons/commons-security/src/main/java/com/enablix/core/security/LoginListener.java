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
import com.enablix.core.domain.tenant.Tenant;
import com.enablix.core.domain.user.User;
import com.enablix.core.security.service.EnablixUserService.LoggedInUser;
import com.enablix.core.system.repo.TenantRepository;
import com.enablix.services.util.ActivityLogger;

@Component
public class LoginListener implements ApplicationListener<AuthenticationSuccessEvent> {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoginListener.class);
	
	@Autowired
	private TenantRepository tenantRepo;
	
	@Override
	public void onApplicationEvent(AuthenticationSuccessEvent event) {
		
		UserDetails ud = (UserDetails) event.getAuthentication().getPrincipal();
		
		LOGGER.info("User {} logged-in successfully", ud.getUsername());
		
		ActivityAudit userLogin = new ActivityAudit();
		
		UserAccountActivity userLoginActvy = new UserAccountActivity(AccountActivityType.LOGIN);
		userLogin.setActivity(userLoginActvy);
		
		userLogin.setActivityTime(Calendar.getInstance().getTime());
		
		RegisteredActor actor = new RegisteredActor();
		actor.setUserId(ud.getUsername());
		userLogin.setActor(actor);

		userLogin.setChannel(new ActivityChannel(Channel.WEB));
		
		if (ud instanceof LoggedInUser) {
			
			User user = ((LoggedInUser) ud).getUser();
			Tenant tenant = tenantRepo.findByTenantId(user.getTenantId());
			
			String templateId = tenant == null ? "" : tenant.getDefaultTemplateId();
			
			// set up process context to fetch user roles from tenant specific database
			ProcessContext.initialize(user.getUserId(), user.getTenantId(), templateId);
			
			try {
				
				ActivityLogger.auditActivity(userLogin);
				
			} finally {
				ProcessContext.clear();
			}
			
		}
	}

}
