package com.enablix.core.domain.activity;

import com.enablix.core.api.RecordReference;

public class ContentActivity extends ContextAwareActivity implements RecordReference {

	public enum ContainerType {
		CONTENT, REF_DATA
	}
	
	private String itemTitle;
	
	private ContainerType containerType;
	
	protected ContentActivity() {
		// for ORM
	}

	public ContentActivity(String itemIdentity, String containerQId, ContainerType containerType,
			ActivityType activityType, String itemTitle) {
		
		super(Category.CONTENT, activityType);

		setItemIdentity(itemIdentity);
		setContainerQId(containerQId);
		this.containerType = containerType;
		this.itemTitle = itemTitle;
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
