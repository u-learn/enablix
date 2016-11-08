package com.enablix.core.domain.activity;

public class ContentSuggestActivity extends ContentActivity {

	private String contentApprovalIdentity;
	
	private String contentDetailIdentity;
	
	public ContentSuggestActivity(String itemIdentity, String containerQId, 
			ContainerType containerType, String itemTitle, ContentActivityType actType,
			String contentApprovalIdentity, String contentDetailIdentity) {
		
		super(itemIdentity, containerQId, containerType, actType, itemTitle);
		
		this.contentApprovalIdentity = contentApprovalIdentity;
		this.contentDetailIdentity = contentDetailIdentity;
	}

	public String getContentApprovalIdentity() {
		return contentApprovalIdentity;
	}

	public void setContentApprovalIdentity(String contentApprovalIdentity) {
		this.contentApprovalIdentity = contentApprovalIdentity;
	}

	public String getContentDetailIdentity() {
		return contentDetailIdentity;
	}

	public void setContentDetailIdentity(String contentDetailIdentity) {
		this.contentDetailIdentity = contentDetailIdentity;
	}
	
}
