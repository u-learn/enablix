package com.enablix.core.domain.activity;

import com.enablix.core.domain.content.kit.ContentKit;

public class ContentKitActivity extends ContextAwareActivity {

	public enum ActivityType {
		
		KIT_ADDED("Kit Added"), KIT_UPDATE("Kit Updated"), KIT_DELETE("Kit Deleted"), 
		KIT_SHARE("Kit Shared"), KIT_ACCESS("Kit Accessed"); 
	
		private String value;

		private ActivityType(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
		
	}
	
	private String itemIdentity;
	
	private String itemTitle;
	
	private ActivityType activityType;
	
	protected ContentKitActivity() {
		super(Category.CONTENT_KIT);
	}

	public ContentKitActivity(String itemIdentity, ActivityType activityType, String itemTitle) {
		super(Category.CONTENT_KIT);
		this.itemIdentity = itemIdentity;
		this.activityType = activityType;
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

	public ActivityType getActivityType() {
		return activityType;
	}

	public void setActivityType(ActivityType activityType) {
		this.activityType = activityType;
	}

	public static ContentKitActivity createActivity(ContentKit contentKit, ActivityType activityType) {
		return new ContentKitActivity(contentKit.getIdentity(), activityType, contentKit.getName());
	}
}
