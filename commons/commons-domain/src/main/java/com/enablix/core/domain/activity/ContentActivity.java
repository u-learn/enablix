package com.enablix.core.domain.activity;

public class ContentActivity extends Activity {

	public enum ContainerType {
		CONTENT, REF_DATA
	}
	
	public enum ContentActivityType {
		
		CONTENT_ADD("Added"), CONTENT_UPDATE("Updated"), CONTENT_DELETE("Deleted"), 
		CONTENT_SHARE("Shared"), CONTENT_ACCESS("Accessed"), DOC_DOWNLOAD("Downloaded"), 
		DOC_UPLOAD("Uploaded"), DOC_PREVIEW("Previewed"), CONTENT_ADD_SUGGEST("Content Request Added"), 
		CONTENT_UPDATE_SUGGEST("Content Request Updated"), CONTENT_SUGGEST_APPROVED("Content Request Approved"), 
		CONTENT_SUGGEST_REJECT("Content Request Rejected"), CONTENT_SUGGEST_WITHDRAW("Content Request Withdrawn"), 
		CONTENT_SUGGEST_EDIT("Content Request Edited"), CONTENT_SUGGEST_VIEW("Content Request Viewed"),
		CONTENT_PORTAL_URL_COPIED("Content Portal URL Copied"),CONTENT_DOWNLD_URL_COPIED("Content Download URL Copied");
		private String value;

		private ContentActivityType(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
		
	}
	
	private String itemIdentity;
	
	private String itemTitle;
	
	private String containerQId;
	
	private ContainerType containerType;
	
	private ContentActivityType activityType;
	
	private String activityOrigin;
	
	protected ContentActivity() {
		super(Category.CONTENT);
	}

	public ContentActivity(String itemIdentity, String containerQId, ContainerType containerType,
			ContentActivityType activityType, String itemTitle) {
		super(Category.CONTENT);
		this.itemIdentity = itemIdentity;
		this.containerQId = containerQId;
		this.containerType = containerType;
		this.activityType = activityType;
		this.itemTitle = itemTitle;
	}

	public String getItemIdentity() {
		return itemIdentity;
	}

	public void setItemIdentity(String itemIdentity) {
		this.itemIdentity = itemIdentity;
	}

	public String getContainerQId() {
		return containerQId;
	}

	public void setContainerQId(String containerQId) {
		this.containerQId = containerQId;
	}

	public ContainerType getContainerType() {
		return containerType;
	}

	public void setContainerType(ContainerType containerType) {
		this.containerType = containerType;
	}

	public ContentActivityType getActivityType() {
		return activityType;
	}

	public void setActivityType(ContentActivityType activityType) {
		this.activityType = activityType;
	}

	public String getItemTitle() {
		return itemTitle;
	}

	public void setItemTitle(String itemTitle) {
		this.itemTitle = itemTitle;
	}

	public String getActivityOrigin() {
		return activityOrigin;
	}

	public void setActivityOrigin(String activityOrigin) {
		this.activityOrigin = activityOrigin;
	}

}
