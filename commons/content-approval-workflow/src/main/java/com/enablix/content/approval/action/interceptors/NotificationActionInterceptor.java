package com.enablix.content.approval.action.interceptors;

import static com.enablix.content.approval.ContentApprovalConstants.ACTION_APPROVE;
import static com.enablix.content.approval.ContentApprovalConstants.ACTION_EDIT;
import static com.enablix.content.approval.ContentApprovalConstants.ACTION_REJECT;
import static com.enablix.content.approval.ContentApprovalConstants.ACTION_SUBMIT;
import static com.enablix.content.approval.ContentApprovalConstants.ACTION_WITHDRAW;

import org.springframework.stereotype.Component;

import com.enablix.commons.util.concurrent.Events;
import com.enablix.content.approval.ContentApprovalConstants;
import com.enablix.content.approval.email.NotificationPayload;
import com.enablix.content.approval.model.ContentApproval;
import com.enablix.content.approval.model.ContentDetail;
import com.enablix.core.mq.Event;
import com.enablix.core.mq.util.EventUtil;
import com.enablix.state.change.impl.ActionInterceptorAdapter;
import com.enablix.state.change.model.ActionInput;

@Component
public class NotificationActionInterceptor extends ActionInterceptorAdapter<ContentDetail, ContentApproval>{

	@Override
	public void onActionComplete(String actionName, ActionInput actionIn, ContentApproval recording) {
		
		NotificationPayload payload = new NotificationPayload(actionIn, recording);
		
		switch(actionName) {
		
			case ACTION_SUBMIT:
				EventUtil.publishEvent(new Event<NotificationPayload>(Events.NOTIFY_NEW_CONTENT_REQUEST, payload));
				break;
			
			case ACTION_APPROVE:
				EventUtil.publishEvent(new Event<NotificationPayload>(Events.NOTIFY_CONTENT_REQUEST_APPROVED, payload));
				break;
			
			case ACTION_REJECT:
				EventUtil.publishEvent(new Event<NotificationPayload>(Events.NOTIFY_CONTENT_REQUEST_REJECTED, payload));
				break;
		}
		
	}

	@Override
	public String[] actionsInterestedIn() {
		return new String[] { ACTION_APPROVE, ACTION_EDIT, ACTION_REJECT, ACTION_SUBMIT, ACTION_WITHDRAW };
	}

	@Override
	public String workflowName() {
		return ContentApprovalConstants.WORKFLOW_NAME;
	}

}
