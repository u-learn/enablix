package com.enablix.core.domain.activity;

import com.enablix.core.api.RecordReference;

public class RecordAwareNavActivity extends ContainerAwareNavActivity implements RecordReference {

	private String itemIdentity;
	
	private String itemTitle;
	
	protected RecordAwareNavActivity() {
		// for ORM
	}
	
	public RecordAwareNavActivity(ActivityType activityType, String pageName, String containerQId, String itemIdentity, String itemTitle) {
		super(activityType, pageName, containerQId);
		this.itemIdentity = itemIdentity;
		this.itemTitle = itemTitle;
	}

	public String getItemIdentity() {
		return itemIdentity;
	}

	public void setItemIdentity(String itemIdentity) {
		this.itemIdentity = itemIdentity;
	}

	public String getItemTitle() {
		return itemTitle;
	}

	public void setItemTitle(String itemTitle) {
		this.itemTitle = itemTitle;
	}

}
