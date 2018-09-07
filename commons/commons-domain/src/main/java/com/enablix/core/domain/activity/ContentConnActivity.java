package com.enablix.core.domain.activity;

public class ContentConnActivity extends Activity {

	private String itemTitle;
	
	@SuppressWarnings("unused")
	private ContentConnActivity() {
		// for ORM
	}
	
	public ContentConnActivity(ActivityType activityType) {
		super(Category.CONTENT_CONNECTION, activityType);
	}
	
	public String getItemTitle() {
		return itemTitle;
	}

	public void setItemTitle(String itemTitle) {
		this.itemTitle = itemTitle;
	}

}
