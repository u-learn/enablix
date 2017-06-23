package com.enablix.core.domain.activity;

public class UserAccountActivity extends Activity {

	public enum AccountActivityType {
		LOGIN, LOGOUT, SLACK_AUTH, SLACK_UNAUTH
	}

	private String ipAddress;
	
	protected UserAccountActivity() {
		// for ORM
	}
	
	public UserAccountActivity(ActivityType accountActivityType) {
		super(Category.USER_ACCOUNT, accountActivityType);
	}
	
	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

}
