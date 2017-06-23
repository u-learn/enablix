package com.enablix.core.domain.activity;

public class NavigationActivity extends ContextAwareActivity {

	private String pageName;
	
	protected NavigationActivity() {
		// for ORM
	}
	
	public NavigationActivity(ActivityType activityType, String pageName) {
		super(Category.NAVIGATION, activityType);
		this.pageName = pageName;
	}

	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}
	
}
