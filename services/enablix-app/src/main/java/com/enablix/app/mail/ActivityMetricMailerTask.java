package com.enablix.app.mail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.mail.generic.EmailRequestProcessor;
import com.enablix.app.mail.generic.GenericMailConstants;
import com.enablix.app.mail.web.EmailRequest;
import com.enablix.app.mail.web.EmailRequest.Recipient;
import com.enablix.commons.util.PermissionConstants;
import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.commons.util.date.DateUtil;
import com.enablix.commons.util.date.Period;
import com.enablix.core.domain.security.authorization.Role;
import com.enablix.core.domain.security.authorization.UserProfile;
import com.enablix.core.mongo.search.service.SearchRequest;
import com.enablix.core.security.auth.repo.RoleRepository;
import com.enablix.core.security.auth.repo.UserProfileRepository;
import com.enablix.task.PerTenantTask;
import com.enablix.task.Task;
import com.enablix.task.TaskContext;

@Component
@PerTenantTask
public class ActivityMetricMailerTask implements Task {

	@Autowired
	private RoleRepository roleRepo;
	
	@Autowired
	private UserProfileRepository userProfileRepo;
	
	@Autowired
	private EmailRequestProcessor emailRequestProcessor;
	
	@Override
	public void run(TaskContext context) {
		
		// need to notify admins with permission to receive activity mail
		// find roles with the permission
		List<Role> adminRoles = roleRepo.findByPermissions(PermissionConstants.PERMISSION_RECEIVE_USER_ACTIVITY_MAIL);
				
		if (!adminRoles.isEmpty()) {
					
			// find the user identities who have these roles
			List<UserProfile> userprofiles = userProfileRepo.findBySystemProfile_RolesIn(adminRoles);
			
			if (CollectionUtil.isNotEmpty(userprofiles)) {
			
				
				List<Recipient> recipients = CollectionUtil.transform(userprofiles, 
												() -> new ArrayList<Recipient>(), 
												(userProf) -> new Recipient(userProf));
				
				Period month = DateUtil.getPreviousMonth();
				
				Map<String, Object> inputData = new HashMap<>();
				inputData.put(GenericMailConstants.IN_DATA_METRIC_START_DATE, month.getStartDate());
				inputData.put(GenericMailConstants.IN_DATA_METRIC_END_DATE, month.getEndDate());
				inputData.put(GenericMailConstants.IN_DATA_MONTH, DateUtil.getMonthName(month.getEndDate()));
				inputData.put(GenericMailConstants.IN_DATA_YEAR, DateUtil.getYear(month.getEndDate()));
				
				EmailRequest emailRequest = new EmailRequest();
				emailRequest.setMailTemplateId(GenericMailConstants.MAIL_TEMPLATE_MONTHLY_TEAM_ACTIVITY);
				emailRequest.setRecipients(recipients);
				emailRequest.setInputData(inputData);
				emailRequest.setDataFilter(new SearchRequest());
				
				emailRequestProcessor.sendEmail(GenericMailConstants.MAIL_TYPE_ACTIVITY_METRIC, emailRequest);
		
			}
		}
	}

	@Override
	public String taskId() {
		return "activity-metric-mailer-task";
	}

}
