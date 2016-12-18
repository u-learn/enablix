package com.enablix.core.domain.activity;

public class ContentConnActivity extends Activity {

	public enum ContentConnActivityType {
		ADDED, UPDATED, DELETED
	}

	private String itemTitle;
	
	private String itemIdentity;
	
	private ContentConnActivityType activityType;

	public ContentConnActivity() {
		super(Category.CONTENT_CONNECTION);
	}
	
	public String getItemTitle() {
		return itemTitle;
	}

	public void setItemTitle(String itemTitle) {
		this.itemTitle = itemTitle;
	}

	public String getItemIdentity() {
		return itemIdentity;
	}

	public void setItemIdentity(String itemIdentity) {
		this.itemIdentity = itemIdentity;
	}

	public ContentConnActivityType getActivityType() {
		return activityType;
	}

	public void setActivityType(ContentConnActivityType activityType) {
		this.activityType = activityType;
	}
	
}
