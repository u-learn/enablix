package com.enablix.content.approval.action.interceptors;

import static com.enablix.content.approval.ContentApprovalConstants.ACTION_APPROVE;
import static com.enablix.content.approval.ContentApprovalConstants.ACTION_EDIT;
import static com.enablix.content.approval.ContentApprovalConstants.ACTION_REJECT;
import static com.enablix.content.approval.ContentApprovalConstants.ACTION_SUBMIT;
import static com.enablix.content.approval.ContentApprovalConstants.ACTION_WITHDRAW;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.enablix.content.approval.ContentApprovalConstants;
import com.enablix.content.approval.ContentApprovalUtil;
import com.enablix.content.approval.model.ContentApproval;
import com.enablix.content.approval.model.ContentDetail;
import com.enablix.content.approval.model.ContentDetail.RequestType;
import com.enablix.core.activity.audit.ActivityTrackingContext;
import com.enablix.core.domain.activity.Activity.ActivityType;
import com.enablix.core.domain.activity.ActivityChannel.Channel;
import com.enablix.core.domain.activity.ContentSuggestActivity;
import com.enablix.services.util.ActivityLogger;
import com.enablix.state.change.impl.ActionInterceptorAdapter;
import com.enablix.state.change.model.ActionInput;

@Component
public class ActivityAuditInterceptor extends ActionInterceptorAdapter<ContentDetail, ContentApproval> {

	private static final Map<String, ActivityType> actionToActivityMap = new HashMap<>();
	private static final Map<RequestType, ActivityType> requestTypeToUpdateActivityMap = new HashMap<>();
	
	static {
		actionToActivityMap.put(ACTION_APPROVE, ActivityType.CONTENT_SUGGEST_APPROVED);
		actionToActivityMap.put(ACTION_EDIT, ActivityType.CONTENT_SUGGEST_EDIT);
		actionToActivityMap.put(ACTION_REJECT, ActivityType.CONTENT_SUGGEST_REJECT);
		actionToActivityMap.put(ACTION_WITHDRAW, ActivityType.CONTENT_SUGGEST_WITHDRAW);
		
		requestTypeToUpdateActivityMap.put(RequestType.ADD, ActivityType.CONTENT_ADD_SUGGEST);
		requestTypeToUpdateActivityMap.put(RequestType.UPDATE, ActivityType.CONTENT_UPDATE_SUGGEST);
		requestTypeToUpdateActivityMap.put(RequestType.ARCHIVE, ActivityType.CONTENT_ARCHIVE_SUGGEST);
		requestTypeToUpdateActivityMap.put(RequestType.UNARCHIVE, ActivityType.CONTENT_UNARCHIVE_SUGGEST);
	}
	
	
	
	
	@Override
	public void onActionComplete(String actionName, ActionInput actionIn, ContentApproval recording) {
		
		ContentSuggestActivity activity = ContentApprovalUtil.createAuditActivityInstance(
				recording, mapActivityType(actionName, recording));
		
		Channel channel = ActivityTrackingContext.get().getActivityChannel(Channel.WEB);
		ActivityLogger.auditContentActivity(activity, channel);
	}
	
	private ActivityType mapActivityType(String actionName, ContentApproval recording) {
		
		switch (actionName) {
			case ACTION_SUBMIT:
				ActivityType activityType = requestTypeToUpdateActivityMap.get(recording.getObjectRef().getRequestType());
				return activityType == null ? ActivityType.CONTENT_ADD_SUGGEST : activityType;
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
