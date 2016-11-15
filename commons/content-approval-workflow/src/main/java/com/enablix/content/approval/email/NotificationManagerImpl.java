package com.enablix.content.approval.email;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.commons.util.collection.CollectionUtil.CollectionCreator;
import com.enablix.commons.util.collection.CollectionUtil.ITransformer;
import com.enablix.commons.util.concurrent.Events;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.content.approval.ContentApprovalConstants;
import com.enablix.content.approval.model.ContentApproval;
import com.enablix.content.approval.model.ContentDetail;
import com.enablix.core.domain.security.authorization.Role;
import com.enablix.core.domain.security.authorization.UserRole;
import com.enablix.core.domain.user.User;
import com.enablix.core.mail.service.MailService;
import com.enablix.core.mq.EventSubscription;
import com.enablix.core.security.auth.repo.RoleRepository;
import com.enablix.core.security.auth.repo.UserRoleRepository;
import com.enablix.core.system.repo.UserRepository;
import com.enablix.state.change.model.SimpleActionInput;

@Component
public class NotificationManagerImpl implements NotificationManager {

	@Autowired
	private NotificationTemplateInputBuilder inputBuilder;
	
	@Autowired
	private RoleRepository roleRepo;
	
	@Autowired
	private UserRoleRepository userRoleRepo;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private MailService mailService;
	
	@Override
	@EventSubscription(eventName = Events.NOTIFY_NEW_CONTENT_REQUEST)
	public void notifyNewRequest(NotificationPayload payload) {
		
		ContentDetail actionInput = (ContentDetail) payload.getActionInput();
		ContentApproval contentRequest = payload.getContentRequest();
		
		// need to notify admins with permission to approve requests
		// find roles with the permission
		List<Role> adminRoles = roleRepo.findByPermissions(
				ContentApprovalConstants.PERMISSION_MNG_CONTENT_REQUEST);
		
		if (!adminRoles.isEmpty()) {
			
			List<String> roleIds = CollectionUtil.transform(adminRoles, new CollectionCreator<List<String>, String>() {

					@Override
					public List<String> create() {
						return new ArrayList<>();
					}
					
				}, new ITransformer<Role, String>() {
	
					@Override
					public String transform(Role in) {
						return in.getId();
					}
					
				});

			// find the user identities who have these roles
			List<UserRole> userRoles = userRoleRepo.findByRolesIdIn(roleIds);
			
			ContentApprovalEmailVelocityInput<ContentDetail> emailInput = null;
			for (UserRole userRole : userRoles) {
				
				// exclude the user who has submitted the request.
				if (!userRole.getUserIdentity().equals(contentRequest.getCreatedBy())) {
					
					// find the user details
					User recipient = userRepo.findByIdentityAndTenantId(
							userRole.getUserIdentity(), ProcessContext.get().getTenantId());
					
					if (recipient != null) {
						
						if (emailInput == null) {
							emailInput = inputBuilder.build(
									ContentApprovalConstants.ACTION_APPROVE, actionInput, contentRequest);
						}
						
						emailInput.setRecipientUser(recipient);
						emailInput.setRecipientUserId(recipient.getUserId());
						
						inputBuilder.prepareContentForEmailToUser(emailInput, recipient.getUserId());
						
						mailService.sendHtmlEmail(emailInput, recipient.getUserId(), 
								ContentApprovalConstants.TEMPLATE_PORTAL_REQ_ADMIN_NOTIF);
					}
				}
			}
		}
		
	}

	@Override
	@EventSubscription(eventName = Events.NOTIFY_CONTENT_REQUEST_APPROVED)
	public void notifyApproval(NotificationPayload payload) {
		
		SimpleActionInput actionInput = (SimpleActionInput) payload.getActionInput();
		ContentApproval contentRequest = payload.getContentRequest();
		
		String recipientUserId = contentRequest.getCreatedBy();
		
		ContentApprovalEmailVelocityInput<SimpleActionInput> emailInput = 
				inputBuilder.build(ContentApprovalConstants.ACTION_APPROVE, actionInput, 
						contentRequest, recipientUserId, recipientUserId);
		
		mailService.sendHtmlEmail(emailInput, recipientUserId, 
				ContentApprovalConstants.TEMPLATE_PORTAL_REQ_APPROVED_NOTIF);
		
	}

	@Override
	@EventSubscription(eventName = Events.NOTIFY_CONTENT_REQUEST_REJECTED)
	public void notifyRejection(NotificationPayload payload) {
		
		SimpleActionInput actionInput = (SimpleActionInput) payload.getActionInput();
		ContentApproval contentRequest = payload.getContentRequest();
		
		String recipientUserId = contentRequest.getCreatedBy();
		
		ContentApprovalEmailVelocityInput<SimpleActionInput> emailInput = 
				inputBuilder.build(ContentApprovalConstants.ACTION_APPROVE, actionInput, 
						contentRequest, recipientUserId, recipientUserId);
		
		mailService.sendHtmlEmail(emailInput, recipientUserId, 
				ContentApprovalConstants.TEMPLATE_PORTAL_REQ_REJECT_NOTIF);
		
		
	}
	
}
