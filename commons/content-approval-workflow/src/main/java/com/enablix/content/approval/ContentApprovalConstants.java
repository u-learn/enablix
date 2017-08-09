package com.enablix.content.approval;

public final class ContentApprovalConstants {

	public static final String WORKFLOW_NAME = "Content Approval";
	
	public static final String ACTION_SAVE_DRAFT = "SAVE_DRAFT";
	public static final String ACTION_SUBMIT = "SUBMIT";
	public static final String ACTION_EDIT = "EDIT";
	public static final String ACTION_APPROVE = "APPROVE";
	public static final String ACTION_REJECT = "REJECT";
	public static final String ACTION_WITHDRAW = "WITHDRAW";
	
	public static final String STATE_DRAFT = "DRAFT";
	public static final String STATE_PENDING_APPROVAL = "PENDING_APPROVAL";
	public static final String STATE_REJECTED = "REJECTED";
	public static final String STATE_APPROVED = "APPROVED";
	public static final String STATE_WITHDRAWN = "WITHDRAWN";
	
	public static final String TEMPLATE_PORTAL_REQ_ADMIN_NOTIF = "portalRequestAdminNotif";
	public static final String TEMPLATE_PORTAL_REQ_APPROVED_NOTIF = "portalRequestApproveRequesterNotif";
	public static final String TEMPLATE_PORTAL_REQ_REJECT_NOTIF = "portalRequestRejectRequesterNotif";

	private ContentApprovalConstants() { 
		// object creation not allowed
	}
	
}
