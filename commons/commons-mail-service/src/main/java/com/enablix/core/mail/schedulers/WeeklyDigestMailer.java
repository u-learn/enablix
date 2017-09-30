package com.enablix.core.mail.schedulers;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.content.ui.NavigableContent;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.domain.activity.ActivityChannel.Channel;
import com.enablix.core.domain.activity.ContentShareActivity.ShareMedium;
import com.enablix.core.domain.security.authorization.UserProfile;
import com.enablix.core.mail.service.MailService;
import com.enablix.core.mail.utility.MailConstants;
import com.enablix.core.mail.velocity.WeeklyDigestScenarioInputBuilder;
import com.enablix.core.mail.velocity.input.WeeklyDigestVelocityInput;
import com.enablix.core.security.auth.repo.UserProfileRepository;
import com.enablix.data.segment.DataSegmentService;
import com.enablix.data.view.DataView;
import com.enablix.services.util.ActivityLogger;
import com.enablix.task.PerTenantTask;
import com.enablix.task.Task;
import com.enablix.task.TaskContext;

@Component
@PerTenantTask
public class WeeklyDigestMailer implements Task {

	private static final Logger LOGGER = LoggerFactory.getLogger(WeeklyDigestMailer.class);
	
	@Autowired
	private MailService mailService;

	@Autowired
	private WeeklyDigestScenarioInputBuilder weeklyDigestScenarioInputBuilder;

	@Autowired
	private UserProfileRepository userProfileRepo;
	
	@Autowired
	private DataSegmentService dataSegmentService;
	
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

	@Override
	public void run(TaskContext context) {
		
		try {
			
			List<UserProfile> users = userProfileRepo.findAll();
			
			for (UserProfile userProfile : users) {
			
				if (userProfile.getSystemProfile().isSendWeeklyDigest()) {
				
					try {

						String templateId = ProcessContext.get().getTemplateId();
						
						DataView userView = dataSegmentService.getDataViewForUserProfileIdentity(userProfile.getIdentity());
						
						WeeklyDigestVelocityInput input = weeklyDigestScenarioInputBuilder.build(userView);
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

	@Override
	public String taskId() {
		return "weekly-digest-mailer";
	}

}
