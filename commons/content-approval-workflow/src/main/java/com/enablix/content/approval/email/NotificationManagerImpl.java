package com.enablix.content.approval.email;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.content.ui.DisplayContext;
import com.enablix.app.template.service.TemplateManager;
import com.enablix.commons.util.PermissionConstants;
import com.enablix.commons.util.concurrent.Events;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.content.approval.ContentApprovalConstants;
import com.enablix.content.approval.model.ContentApproval;
import com.enablix.content.approval.model.ContentDetail;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.domain.security.authorization.Role;
import com.enablix.core.domain.security.authorization.UserProfile;
import com.enablix.core.mail.service.MailService;
import com.enablix.core.mongo.view.MongoDataView;
import com.enablix.core.mq.EventSubscription;
import com.enablix.core.security.auth.repo.RoleRepository;
import com.enablix.core.security.auth.repo.UserProfileRepository;
import com.enablix.data.segment.DataSegmentService;
import com.enablix.data.view.DataView;
import com.enablix.services.util.DataViewUtil;
import com.enablix.state.change.model.SimpleActionInput;

@Component
public class NotificationManagerImpl implements NotificationManager {

	@Autowired
	private NotificationTemplateInputBuilder inputBuilder;
	
	@Autowired
	private RoleRepository roleRepo;
	
	@Autowired
	private UserProfileRepository userProfileRepo;
	
	@Autowired
	private MailService mailService;
	
	@Autowired
	private DataSegmentService dataSegmentService;
	
	@Autowired
	private TemplateManager templateManager;
	
	@Override
	@EventSubscription(eventName = Events.NOTIFY_NEW_CONTENT_REQUEST)
	public void notifyNewRequest(NotificationPayload payload) {
		
		ContentDetail actionInput = (ContentDetail) payload.getActionInput();
		ContentApproval contentRequest = payload.getContentRequest();
		
		TemplateFacade template = templateManager.getTemplateFacade(ProcessContext.get().getTemplateId());
		String contentCollection = template.getCollectionName(contentRequest.getObjectRef().getContentQId());
		
		// need to notify admins with permission to approve requests
		// find roles with the permission
		List<Role> adminRoles = roleRepo.findByPermissions(
				PermissionConstants.PERMISSION_MNG_CONTENT_REQUEST);
		
		if (!adminRoles.isEmpty()) {
			
			/*List<String> roleIds = CollectionUtil.transform(adminRoles, new CollectionCreator<List<String>, String>() {

					@Override
					public List<String> create() {
						return new ArrayList<>();
					}
					
				}, new ITransformer<Role, String>() {
	
					@Override
					public String transform(Role in) {
						return in.getId();
					}
					
				});*/

			// find the user identities who have these roles
			List<UserProfile> userprofiles = userProfileRepo.findBySystemProfile_RolesIn(adminRoles);
			
			ContentApprovalEmailVelocityInput<ContentDetail> emailInput = null;
			DisplayContext ctx = new DisplayContext();
			
			for (UserProfile userProfile : userprofiles) {
				
				// exclude the user who has submitted the request.
				if (!userProfile.getEmail().equals(contentRequest.getCreatedBy())) {
					
					DataView userView = dataSegmentService.getDataViewForUserProfileIdentity(userProfile.getIdentity());
					MongoDataView view = DataViewUtil.getMongoDataView(userView);
					
					Map<String, Object> record = contentRequest.getObjectRef().getData();
					
					if (view.isRecordVisible(contentCollection, record)) { 
					
						if (emailInput == null) {
							emailInput = inputBuilder.build(
									ContentApprovalConstants.ACTION_APPROVE, actionInput, contentRequest, userView);
						}
						
						emailInput.setRecipientUser(userProfile);
						emailInput.setRecipientUserId(userProfile.getEmail());
						
						inputBuilder.prepareContentForEmailToUser(emailInput, userProfile.getEmail(), ctx);
						
						mailService.sendHtmlEmail(emailInput, userProfile.getEmail(), 
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
						contentRequest, recipientUserId, recipientUserId, DataViewUtil.allDataView());
		
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
						contentRequest, recipientUserId, recipientUserId, DataViewUtil.allDataView());
		
		mailService.sendHtmlEmail(emailInput, recipientUserId, 
				ContentApprovalConstants.TEMPLATE_PORTAL_REQ_REJECT_NOTIF);
		
	}
	
}
