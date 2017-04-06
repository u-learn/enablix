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
import com.enablix.core.domain.security.authorization.UserProfile;
import com.enablix.core.domain.tenant.Tenant;
import com.enablix.core.mail.service.MailService;
import com.enablix.core.mail.utility.MailConstants;
import com.enablix.core.mail.velocity.WeeklyDigestScenarioInputBuilder;
import com.enablix.core.mail.velocity.input.WeeklyDigestVelocityInput;
import com.enablix.core.mongo.util.MultiTenantExecutor;
import com.enablix.core.mongo.util.MultiTenantExecutor.TenantTask;
import com.enablix.core.security.auth.repo.UserProfileRepository;
import com.enablix.core.system.repo.TenantRepository;
import com.enablix.data.segment.DataSegmentService;
import com.enablix.services.util.ActivityLogger;

@Component
public class MailScheduler {

	private static final Logger LOGGER = LoggerFactory.getLogger(MailScheduler.class);
	
	@Autowired
	private MailService mailService;

	@Autowired
	private WeeklyDigestScenarioInputBuilder weeklyDigestScenarioInputBuilder;

	@Autowired
	private TenantRepository tenantRepo;

	@Autowired
	private UserProfileRepository userProfileRepo;
	
	@Scheduled(cron = "${weekly.digest.timing}")
	public void scheduleWeeklyDigest() {


		List<Tenant> tenants = tenantRepo.findAll();
		
		try {
			
			MultiTenantExecutor.executeForEachTenant(tenants, new TenantTask() {
	
				@Override
				public void execute() {
					
					try {
						
						List<UserProfile> users = userProfileRepo.findAll();
						
						for (UserProfile userProfile : users) {
						
							if (userProfile.getSystemProfile().isSendWeeklyDigest()) {
							
								try {
									/*
									 * Authentication authenticate =
									 * authenticationManager.authenticate( new
									 * UsernamePasswordAuthenticationToken(user.getUserId(),user
									 * .getPassword())); if (authenticate.isAuthenticated())
									 * SecurityContextHolder.getContext().setAuthentication(
									 * authenticate);
									 */
									String templateId = ProcessContext.get().getTemplateId();
									
									WeeklyDigestVelocityInput input = weeklyDigestScenarioInputBuilder.build();
									input.setRecipientUser(userProfile);
									
									if (!input.getRecentList().get("RecentlyUpdated").isEmpty()) {
										String userEmail = userProfile.getEmail();
										mailService.sendHtmlEmail(input, userEmail, MailConstants.SCENARIO_WEEKLY_DIGEST);
										auditContentShare(templateId, input, userEmail);
									}
									
								} catch (Throwable e) {
									
									LOGGER.error("Error sending weekly digest for user: {}", userProfile.getIdentity());
									LOGGER.error("Exception: ", e);
									
								}
							}
						}
						
					} catch (Throwable e) {
						LOGGER.error("Error sending email for tenant: " + ProcessContext.get().getTenantId(), e);
					}
				}
				
			});
			
		} catch (Throwable e) {
			LOGGER.error("Error sending weekly digest: ", e);
		}
		
	}
	
	private void auditContentShare(String templateId, WeeklyDigestVelocityInput sharedContent, String sharedWithEmailId) {
		
		List<ContentDataRef> sharedContentList = new ArrayList<>();
		
		for (List<NavigableContent> contentList : sharedContent.getRecentList().values()) {
			for (NavigableContent content : contentList) {
				sharedContentList.add(ContentDataRef.createContentRef(templateId, content.getQualifiedId(), 
						content.getIdentity(), content.getLabel()));
			}
		}
		
		ActivityLogger.auditContentShare(templateId, sharedContentList, ShareMedium.WEEKLY_DIGEST, 
				Channel.EMAIL, sharedContent.getIdentity(), sharedWithEmailId, null);
	}

}
