package com.enablix.core.domain.activity;

import com.enablix.core.domain.content.kit.ContentKit;

public class ContentKitActivity extends Activity {

	public enum ActivityType {
		
		KIT_ADDED ("Added"), KIT_UPDATE("Updated"), KIT_DELETE("Deleted"), 
		KIT_SHARE("Shared"), KIT_ACCESS("Accessed"); 
	
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
	
	private String activityOrigin;
	
	private String contextName;
	
	private String contextId;
	
	private String contextTerm;
	
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

	public String getActivityOrigin() {
		return activityOrigin;
	}

	public void setActivityOrigin(String activityOrigin) {
		this.activityOrigin = activityOrigin;
	}

	public String getContextName() {
		return contextName;
	}

	public void setContextName(String contextName) {
		this.contextName = contextName;
	}

	public String getContextId() {
		return contextId;
	}

	public void setContextId(String contextId) {
		this.contextId = contextId;
	}

	public String getContextTerm() {
		return contextTerm;
	}

	public void setContextTerm(String contextTerm) {
		this.contextTerm = contextTerm;
	}

	public static ContentKitActivity createActivity(ContentKit contentKit, ActivityType activityType) {
		return new ContentKitActivity(contentKit.getIdentity(), activityType, contentKit.getName());
	}
}
