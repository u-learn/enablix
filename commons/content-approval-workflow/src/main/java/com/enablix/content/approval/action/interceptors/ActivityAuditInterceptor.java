package com.enablix.content.approval.action.interceptors;

import static com.enablix.content.approval.ContentApprovalConstants.ACTION_APPROVE;
import static com.enablix.content.approval.ContentApprovalConstants.ACTION_EDIT;
import static com.enablix.content.approval.ContentApprovalConstants.ACTION_REJECT;
import static com.enablix.content.approval.ContentApprovalConstants.ACTION_SUBMIT;
import static com.enablix.content.approval.ContentApprovalConstants.ACTION_WITHDRAW;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.content.approval.ContentApprovalConstants;
import com.enablix.content.approval.model.ContentApproval;
import com.enablix.content.approval.model.ContentDetail;
import com.enablix.core.domain.activity.ActivityChannel.Channel;
import com.enablix.core.domain.activity.ContentActivity.ContainerType;
import com.enablix.core.domain.activity.ContentActivity.ContentActivityType;
import com.enablix.core.domain.activity.ContentSuggestActivity;
import com.enablix.services.util.ActivityLogger;
import com.enablix.state.change.impl.ActionInterceptorAdapter;
import com.enablix.state.change.model.ActionInput;

@Component
public class ActivityAuditInterceptor extends ActionInterceptorAdapter<ContentDetail, ContentApproval> {

	private static final Map<String, ContentActivityType> actionToActivityMap = new HashMap<>();
	
	static {
		actionToActivityMap.put(ACTION_APPROVE, ContentActivityType.CONTENT_SUGGEST_APPROVED);
		actionToActivityMap.put(ACTION_EDIT, ContentActivityType.CONTENT_SUGGEST_EDIT);
		actionToActivityMap.put(ACTION_REJECT, ContentActivityType.CONTENT_SUGGEST_REJECT);
		actionToActivityMap.put(ACTION_WITHDRAW, ContentActivityType.CONTENT_SUGGEST_WITHDRAW);
	}
	
	
	@Override
	public void onActionComplete(String actionName, ActionInput actionIn, ContentApproval recording) {
		
		ContentDetail objectRef = recording.getObjectRef();

		ContentSuggestActivity activity = new ContentSuggestActivity(
				(String) objectRef.getData().get(ContentDataConstants.IDENTITY_KEY), 
				objectRef.getContentQId(), ContainerType.CONTENT, objectRef.getContentTitle(), 
				mapContentActivityType(actionName, recording), recording.getIdentity(), objectRef.getIdentity());
		
		ActivityLogger.auditContentActivity(activity, Channel.WEB);
	}
	
	private ContentActivityType mapContentActivityType(String actionName, ContentApproval recording) {
		
		switch (actionName) {
			case ACTION_SUBMIT:
				return recording.getObjectRef().isAddRequest() ? 
						ContentActivityType.CONTENT_ADD_SUGGEST : ContentActivityType.CONTENT_UPDATE_SUGGEST;
			default:
				return actionToActivityMap.get(actionName);
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
