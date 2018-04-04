package com.enablix.core.domain.activity;

import com.enablix.core.api.RecordReference;

public class ContentActivity extends ContextAwareActivity implements RecordReference {

	public enum ContainerType {
		CONTENT, REF_DATA
	}
	
	private String itemIdentity;
	
	private String itemTitle;
	
	private String containerQId;
	
	private ContainerType containerType;
	
	protected ContentActivity() {
		// for ORM
	}

	public ContentActivity(String itemIdentity, String containerQId, ContainerType containerType,
			ActivityType activityType, String itemTitle) {
		
		super(Category.CONTENT, activityType);

		this.itemIdentity = itemIdentity;
		this.containerQId = containerQId;
		this.containerType = containerType;
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

	public String getItemTitle() {
		return itemTitle;
	}

	public void setItemTitle(String itemTitle) {
		this.itemTitle = itemTitle;
	}

}
