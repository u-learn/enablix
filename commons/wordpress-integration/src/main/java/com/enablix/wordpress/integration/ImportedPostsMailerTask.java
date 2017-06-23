package com.enablix.wordpress.integration;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.commons.util.PermissionConstants;
import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.commons.util.tenant.TenantUtil;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.domain.activity.Activity.ActivityType;
import com.enablix.core.domain.activity.ActivityAudit;
import com.enablix.core.domain.activity.ContentActivity;
import com.enablix.core.domain.security.authorization.Role;
import com.enablix.core.domain.security.authorization.UserProfile;
import com.enablix.core.mail.service.MailService;
import com.enablix.core.mongo.audit.repo.ActivityAuditRepository;
import com.enablix.core.security.auth.repo.RoleRepository;
import com.enablix.core.security.auth.repo.UserProfileRepository;
import com.enablix.data.segment.DataSegmentService;
import com.enablix.data.view.DataView;
import com.enablix.task.PerTenantTask;
import com.enablix.task.Task;
import com.enablix.task.TaskContext;
import com.enablix.wordpress.info.mail.WPImportMailInputBuilder;
import com.enablix.wordpress.info.mail.WordpressImportMailInput;

@Component
@PerTenantTask
public class ImportedPostsMailerTask implements Task {

	private static final Logger LOGGER = LoggerFactory.getLogger(ImportedPostsMailerTask.class);
	
	@Autowired
	private ActivityAuditRepository activityRepo;
	
	@Autowired
	private RoleRepository roleRepo;
	
	@Autowired
	private UserProfileRepository userProfileRepo;
	
	@Autowired
	private WPImportMailInputBuilder inputBuilder;
	
	@Autowired
	private MailService mailService;
	
	@Autowired
	private DataSegmentService dataSegmentService;
	
	@Override
	public void run(TaskContext context) {
		
		Date lastRunDate = null;
		String tenantId = TenantUtil.getTenantId();
		
		Object lastRun = context.getTaskParameter(tenantId, 
				WordpressConstants.WP_IMPORT_POST_MAILER_TASK_LAST_RUN_PARAM);
		
		if (lastRun instanceof Date) {
			lastRunDate = (Date) lastRun;
		}
		
		if (lastRunDate == null) {
			lastRunDate = new Date(0); 
		}

		Date currentRunDate = Calendar.getInstance().getTime();
		
		LOGGER.debug("Looking for Wordpress post import betwee: {}, and {}", lastRunDate, currentRunDate);
		
		List<ActivityAudit> activities = activityRepo.findByActivityContextNameAndActivityTypeAndCreatedAtBetween(
				WordpressConstants.WP_POST_IMPORT_ACTVY_CTX, 
				ActivityType.CONTENT_ADD.toString(),
				lastRunDate, currentRunDate);
		
		long importCnt = 0;
		
		if (CollectionUtil.isNotEmpty(activities)) {
			sendMail(activities);
			importCnt = activities.size();
		} 
		
		LOGGER.info("Wordpress posts imported [{}]: {}", currentRunDate, importCnt);
		
		context.updateTaskParameter(tenantId, 
				WordpressConstants.WP_POST_FEEDER_TASK_LAST_RUN_PARAM, currentRunDate);

	}

	private void sendMail(List<ActivityAudit> activities) {
		
		String templateId = ProcessContext.get().getTemplateId();
		
		// need to notify admins with permission to approve requests
		// find roles with the permission
		List<Role> adminRoles = roleRepo.findByPermissions(PermissionConstants.PERMISSION_MNG_CONTENT_REQUEST);
		
		if (!adminRoles.isEmpty()) {
			
			// find the user identities who have these roles
			List<UserProfile> userprofiles = userProfileRepo.findBySystemProfile_RolesIn(adminRoles);
			
			List<ContentDataRef> contentList =
					CollectionUtil.transform(activities, 
							() -> new ArrayList<ContentDataRef>(), 
							(in) -> {
								ContentActivity contentActvy = (ContentActivity) in.getActivity();
								return ContentDataRef.createContentRef(templateId, contentActvy.getContainerQId(), 
										contentActvy.getItemIdentity(), contentActvy.getItemTitle());
							});
			
			WordpressImportMailInput emailInput = null;

			for (UserProfile userProfile : userprofiles) {
				
				DataView userView = dataSegmentService.getDataViewForUserProfileIdentity(userProfile.getIdentity());
				
				if (emailInput == null) {
					emailInput = inputBuilder.build(contentList, userView);
				}
				
				emailInput.setRecipientUser(userProfile);
				emailInput.setRecipientUserId(userProfile.getEmail());
				
				mailService.sendHtmlEmail(emailInput, userProfile.getEmail(), 
						WordpressConstants.TEMPLATE_WP_IMPORT_MAILER);
					
			}
		}
	}

	@Override
	public String taskId() {
		return "wp-imported-post-mailer-task";
	}

}
