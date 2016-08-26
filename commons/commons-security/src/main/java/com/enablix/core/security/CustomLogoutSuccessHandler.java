package com.enablix.core.security;

import java.io.IOException;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.domain.activity.ActivityAudit;
import com.enablix.core.domain.activity.ActivityChannel;
import com.enablix.core.domain.activity.ActivityChannel.Channel;
import com.enablix.core.domain.activity.RegisteredActor;
import com.enablix.core.domain.activity.UserAccountActivity;
import com.enablix.core.domain.activity.UserAccountActivity.AccountActivityType;
import com.enablix.core.domain.user.User;
import com.enablix.core.security.service.EnablixUserService.LoggedInUser;
import com.enablix.services.util.ActivityLogger;

public class CustomLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomLogoutSuccessHandler.class);
	
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		super.handle(request, response, authentication);
		
		UserDetails ud = (UserDetails) authentication.getPrincipal();
		
		LOGGER.info("User {} logged-out successfully", ud.getUsername());
		
		ActivityAudit userLogout = new ActivityAudit();
		
		UserAccountActivity userLoginActvy = new UserAccountActivity(AccountActivityType.LOGOUT);
		userLogout.setActivity(userLoginActvy);
		
		userLogout.setActivityTime(Calendar.getInstance().getTime());
		
		RegisteredActor actor = new RegisteredActor(ud.getUsername());
		userLogout.setActor(actor);

		userLogout.setChannel(new ActivityChannel(Channel.WEB));
		
		if (ud instanceof LoggedInUser) {
			
			User user = ((LoggedInUser) ud).getUser();
			
			// set up process context to fetch user roles from tenant specific database
			ProcessContext.initialize(user.getUserId(), user.getTenantId(), null);
			
			try {
				
				ActivityLogger.auditActivity(userLogout);
				
			} finally {
				ProcessContext.clear();
			}
			
		}
		
	}
	
}