package com.enablix.core.domain.activity;

public abstract class Activity {

	public enum Category {
		USER_ACCOUNT, CONTENT, SEARCH, NAVIGATION, CONTENT_CONNECTION
	}
	
	protected Category category;
	
	protected Activity(Category category) {
		this.category = category;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}
	
}
