package com.enablix.core.domain.activity;

import com.enablix.core.api.RecordReference;

public class RecordAwareNavActivity extends ContainerAwareNavActivity implements RecordReference {

	private String itemTitle;
	
	protected RecordAwareNavActivity() {
		// for ORM
	}
	
	public RecordAwareNavActivity(ActivityType activityType, String pageName, String containerQId, String itemIdentity, String itemTitle) {
		super(activityType, pageName, containerQId);
		setItemIdentity(itemIdentity);
		this.itemTitle = itemTitle;
	}

	public String getItemTitle() {
		return itemTitle;
	}

	public void setItemTitle(String itemTitle) {
		this.itemTitle = itemTitle;
	}

}
