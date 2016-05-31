package com.enablix.core.mail.schedulers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.AuthenticationException;

import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.domain.user.User;
import com.enablix.core.mail.service.MailService;
import com.enablix.core.mail.utility.MailConstants;
import com.enablix.core.mail.velocity.WeeklyDigestScenarioInputBuilder;
import com.enablix.core.mail.velocity.input.WeeklyDigestVelocityInput;
import com.enablix.core.system.repo.TenantRepository;
import com.enablix.core.system.repo.UserRepository;

@Configuration
@EnableScheduling
public class MailScheduler {

	@Autowired
	MailService mailService;

	@Autowired
	private WeeklyDigestScenarioInputBuilder weeklyDigestScenarioInputBuilder;

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private TenantRepository tenantRepo;

	/*@Resource(name = "authenticationManager")
	private AuthenticationManager authenticationManager;
*/
	
	@Scheduled(cron = "${weekly.digest.timing}")
	//@Scheduled(fixedDelay = 50000)
	public void scheduleWeeklyDigest() {
		String loggedInUser = null;
		String loggedInTenant = null;

		if (ProcessContext.get() != null) {
			loggedInTenant = ProcessContext.get().getTenantId();
			loggedInUser = ProcessContext.get().getUserId();
			ProcessContext.clear();
		}

		List<User> users = userRepo.findAll();
		for (User user : users) {
			if (user.getProfile().isSendWeeklyDigest()) {
				try {
					/*
					 * Authentication authenticate =
					 * authenticationManager.authenticate( new
					 * UsernamePasswordAuthenticationToken(user.getUserId(),user
					 * .getPassword())); if (authenticate.isAuthenticated())
					 * SecurityContextHolder.getContext().setAuthentication(
					 * authenticate);
					 */

					ProcessContext.initialize(user.getUserId(), user.getTenantId(), tenantRepo.findByTenantId(user.getTenantId()).getDefaultTemplateId());
					WeeklyDigestVelocityInput input = weeklyDigestScenarioInputBuilder.build(user.getTenantId());
					if (!input.getRecentList().get("RecentlyUpdated").isEmpty())
						mailService.sendHtmlEmail(input, user.getUserId(), MailConstants.SCENARIO_WEEKLY_DIGEST);
					ProcessContext.clear();
				} catch (AuthenticationException e) {
					e.printStackTrace();
					ProcessContext.clear();
				}
			}
		}
		if(loggedInUser!=null && loggedInTenant!=null)
			ProcessContext.initialize(loggedInUser, loggedInTenant, null);

	}

}
