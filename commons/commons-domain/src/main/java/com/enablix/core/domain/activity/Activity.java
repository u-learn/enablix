package com.enablix.core.domain.activity;

public abstract class Activity {

	public enum Category {
		USER_ACCOUNT, CONTENT, CONTENT_KIT, SEARCH, NAVIGATION, CONTENT_CONNECTION
	}
	
	public enum ActivityType {
		
		/* Content related activities */
		CONTENT_ADD(Category.CONTENT, "Added"), CONTENT_UPDATE(Category.CONTENT, "Updated"), 
		CONTENT_DELETE(Category.CONTENT, "Deleted"), CONTENT_SHARE(Category.CONTENT, "Shared"), 
		CONTENT_ACCESS(Category.CONTENT, "Accessed"), DOC_DOWNLOAD(Category.CONTENT, "Downloaded"),
		CONTENT_ARCHIVED(Category.CONTENT, "Archived"), CONTENT_UNARCHIVED(Category.CONTENT, "Un-archived"), 
		DOC_UPLOAD(Category.CONTENT, "Uploaded"), DOC_PREVIEW(Category.CONTENT, "Previewed"), 
		CONTENT_ADD_SUGGEST(Category.CONTENT, "Content Add Request"), 
		CONTENT_UPDATE_SUGGEST(Category.CONTENT, "Content Update Request"),
		CONTENT_ARCHIVE_SUGGEST(Category.CONTENT, "Content Archive Request"), 
		CONTENT_UNARCHIVE_SUGGEST(Category.CONTENT, "Content Un-archive Request"),
		CONTENT_SUGGEST_APPROVED(Category.CONTENT, "Content Request Approved"), 
		CONTENT_SUGGEST_REJECT(Category.CONTENT, "Content Request Rejected"), 
		CONTENT_SUGGEST_WITHDRAW(Category.CONTENT, "Content Request Withdrawn"), 
		CONTENT_SUGGEST_EDIT(Category.CONTENT, "Content Request Edited"), 
		CONTENT_SUGGEST_VIEW(Category.CONTENT, "Content Request Viewed"),
		CONTENT_PORTAL_URL_COPIED(Category.CONTENT, "Content Portal URL Copied"), 
		CONTENT_DOWNLD_URL_COPIED(Category.CONTENT, "Content Download URL Copied"),
		CONTENT_EXT_LINK_URL_COPIED(Category.CONTENT, "Content External Link URL Copied"),
		CONTENT_EMBED_CODE_COPIED(Category.CONTENT, "Content Embed Code Copied"),
		CONTENT_EMBED_VIEW(Category.CONTENT, "Content Embed View"),
		
		/* Account related activities */
		LOGIN(Category.USER_ACCOUNT, "Login"), LOGOUT(Category.USER_ACCOUNT, "Logout"), 
		SLACK_AUTH(Category.USER_ACCOUNT, "Slack Authorization"), SLACK_UNAUTH(Category.USER_ACCOUNT, "Slack Un-authorization"),
		
		/* Content connection related activities */
		ADDED(Category.CONTENT_CONNECTION, "Content Connection Added"), 
		UPDATED(Category.CONTENT_CONNECTION, "Content Connection Updated"), 
		DELETED(Category.CONTENT_CONNECTION, "Content Connection Deleted"),
		
		/* Navigation activities */
		NAV_EXTERNAL_LINK(Category.NAVIGATION, "External Url Click"),
		
		/* Search activities */
		SEARCH_FREE_TEXT(Category.SEARCH, "Search"), SUGGESTED_SEARCH(Category.SEARCH, "Suggested search")
		
		;
		
		private Category category;
		private String value;

		private ActivityType(Category category, String value) {
			this.category = category;
			this.value = value;
		}

		public String getValue() {
			return value;
		}

		public Category getCategory() {
			return category;
		}
		
	}
	
	protected Category category;
	
	protected ActivityType activityType; 
	
	// required for aggregation to work
	private String itemIdentity;
	
	// required for aggregation to work
	private String containerQId;
	
	protected Activity() {
		// for ORM
	}
	
	protected Activity(Category category, ActivityType activityType) {
		this.category = category;
		this.activityType = activityType;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public ActivityType getActivityType() {
		return activityType;
	}

	public void setActivityType(ActivityType activityType) {
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

}
