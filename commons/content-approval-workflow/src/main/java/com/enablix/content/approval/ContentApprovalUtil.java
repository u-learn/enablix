package com.enablix.content.approval;

import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.content.approval.model.ContentApproval;
import com.enablix.content.approval.model.ContentDetail;
import com.enablix.core.domain.activity.Activity.ActivityType;
import com.enablix.core.domain.activity.ContentActivity.ContainerType;
import com.enablix.core.domain.activity.ContentSuggestActivity;

public class ContentApprovalUtil {

	public static ContentSuggestActivity createAuditActivityInstance(ContentApproval content, ActivityType activityType) {
		
		ContentDetail objectRef = content.getObjectRef();

		ContentSuggestActivity activity = new ContentSuggestActivity(
				(String) objectRef.getData().get(ContentDataConstants.IDENTITY_KEY), 
				objectRef.getContentQId(), ContainerType.CONTENT, objectRef.getContentTitle(), 
				activityType, content.getIdentity(), objectRef.getIdentity());
		
		return activity;
	}
	
}
