package com.enablix.core.mail.schedulers;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.enablix.app.content.ui.NavigableContent;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.domain.activity.ActivityChannel.Channel;
import com.enablix.core.domain.activity.ContentShareActivity.ShareMedium;
import com.enablix.core.domain.user.User;
import com.enablix.core.mail.service.MailService;
import com.enablix.core.mail.utility.MailConstants;
import com.enablix.core.mail.velocity.WeeklyDigestScenarioInputBuilder;
import com.enablix.core.mail.velocity.input.WeeklyDigestVelocityInput;
import com.enablix.core.system.repo.TenantRepository;
import com.enablix.core.system.repo.UserRepository;
import com.enablix.services.util.ActivityLogger;

@Component
public class MailScheduler {

	private static final Logger LOGGER = LoggerFactory.getLogger(MailScheduler.class);
	
	@Autowired
	private MailService mailService;

	@Autowired
	private WeeklyDigestScenarioInputBuilder weeklyDigestScenarioInputBuilder;

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private TenantRepository tenantRepo;

	@Scheduled(cron = "${weekly.digest.timing}")
	public void scheduleWeeklyDigest() {

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

					String templateId = tenantRepo.findByTenantId(user.getTenantId()).getDefaultTemplateId();
					ProcessContext.initialize(user.getUserId(), user.getDisplayName(), user.getTenantId(), templateId);
					
					WeeklyDigestVelocityInput input = weeklyDigestScenarioInputBuilder.build();
					if (!input.getRecentList().get("RecentlyUpdated").isEmpty()) {
						mailService.sendHtmlEmail(input, user.getUserId(), MailConstants.SCENARIO_WEEKLY_DIGEST);
						auditContentShare(templateId, input, user.getUserId());
					}
					
				} catch (Throwable e) {
					
					LOGGER.error("Error sending weekly digest for user: {}", user.getUserId());
					LOGGER.error("Exception: ", e);
					
				} finally {
					ProcessContext.clear();
				}
			}
		}
		
	}
	
	private void auditContentShare(String templateId, WeeklyDigestVelocityInput sharedContent, String sharedWithEmailId) {
		
		List<ContentDataRef> sharedContentList = new ArrayList<>();
		
		for (List<NavigableContent> contentList : sharedContent.getRecentList().values()) {
			for (NavigableContent content : contentList) {
				sharedContentList.add(new ContentDataRef(templateId, content.getQualifiedId(), 
						content.getIdentity(), content.getLabel()));
			}
		}
		
		ActivityLogger.auditContentShare(templateId, sharedContentList, ShareMedium.WEEKLY_DIGEST, 
				Channel.EMAIL, sharedContent.getIdentity(), sharedWithEmailId, null);
	}

}
