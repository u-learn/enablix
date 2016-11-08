package com.enablix.core.domain.activity;

public class ContentActivity extends Activity {

	public enum ContainerType {
		CONTENT, REF_DATA
	}
	
	public enum ContentActivityType {
		CONTENT_ADD, CONTENT_UPDATE, CONTENT_DELETE, CONTENT_SHARE, CONTENT_ACCESS, DOC_DOWNLOAD, DOC_UPLOAD, DOC_PREVIEW,
		CONTENT_ADD_SUGGEST, CONTENT_UPDATE_SUGGEST, CONTENT_SUGGEST_APPROVED, CONTENT_SUGGEST_REJECT, CONTENT_SUGGEST_WITHDRAW, CONTENT_SUGGEST_EDIT
	}
	
	private String itemIdentity;
	
	private String itemTitle;
	
	private String containerQId;
	
	private ContainerType containerType;
	
	private ContentActivityType activityType;
	
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

}
