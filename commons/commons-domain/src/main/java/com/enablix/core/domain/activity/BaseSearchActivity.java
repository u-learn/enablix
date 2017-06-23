package com.enablix.core.domain.activity;

public class BaseSearchActivity extends ContextAwareActivity {

	private String searchTerm;

	protected BaseSearchActivity() {
		// for ORM
	}
	
	public BaseSearchActivity(ActivityType activityType, String searchTerm) {
		super(Category.SEARCH, activityType);
		this.searchTerm = searchTerm;
	}

	public String getSearchTerm() {
		return searchTerm;
	}

	public void setSearchTerm(String searchTerm) {
		this.searchTerm = searchTerm;
	}
	
}
