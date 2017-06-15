package com.enablix.core.domain.activity;

public class NavigationActivity extends ContextAwareActivity {

	private String pageName;
	
	protected NavigationActivity() {
		// for ORM
		super(Category.NAVIGATION);
	}
	
	public NavigationActivity(String pageName) {
		this();
		this.pageName = pageName;
	}

	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}
	
}
