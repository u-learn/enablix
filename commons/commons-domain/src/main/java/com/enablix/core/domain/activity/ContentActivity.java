package com.enablix.core.domain.activity;

public class ContentActivity extends Activity {

	public enum ContainerType {
		CONTENT, REF_DATA
	}
	
	public enum ContentActivityType {
		CONTENT_ADD, CONTENT_UPDATE, CONTENT_DELETE, CONTENT_SHARE, CONTENT_ACCESS
	}
	
	private String itemIdentity;
	
	private String containerQId;
	
	private ContainerType containerType;
	
	private ContentActivityType activityType;

	public ContentActivity(String itemIdentity, String containerQId, ContainerType containerType,
			ContentActivityType activityType) {
		super(Category.CONTENT);
		this.itemIdentity = itemIdentity;
		this.containerQId = containerQId;
		this.containerType = containerType;
		this.activityType = activityType;
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

}
