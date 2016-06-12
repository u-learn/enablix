package com.enablix.core.domain.activity;

public abstract class Activity {

	public enum Category {
		USER_ACCOUNT, CONTENT
	}
	
	public abstract Category getCategory();
	
}
