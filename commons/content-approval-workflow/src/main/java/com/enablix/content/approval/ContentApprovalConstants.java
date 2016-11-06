package com.enablix.content.approval;

public final class ContentApprovalConstants {

	public static final String WORKFLOW_NAME = "Content Approval";
	
	public static final String ACTION_SUBMIT = "SUBMIT";
	public static final String ACTION_EDIT = "EDIT";
	public static final String ACTION_APPROVE = "APPROVE";
	public static final String ACTION_REJECT = "REJECT";
	public static final String ACTION_WITHDRAW = "WITHDRAW";
	
	public static final String STATE_PENDING_APPROVAL = "PENDING_APPROVAL";
	public static final String STATE_REJECTED = "REJECTED";
	public static final String STATE_APPROVED = "APPROVED";
	public static final String STATE_WITHDRAWN = "WITHDRAWN";

	private ContentApprovalConstants() { 
		// object creation not allowed
	}
	
}
