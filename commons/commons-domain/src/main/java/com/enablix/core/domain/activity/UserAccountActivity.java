package com.enablix.core.domain.activity;

public class UserAccountActivity extends Activity {

	public enum AccountActivityType {
		LOGIN, LOGOUT,SLACK_AUTH,SLACK_UNAUTH
	}

	private AccountActivityType accountActivityType;
	
	private String ipAddress;
	
	public UserAccountActivity(AccountActivityType accountActivityType) {
		super(Category.USER_ACCOUNT);
		this.accountActivityType = accountActivityType;
	}
	
	public AccountActivityType getAccountActivityType() {
		return accountActivityType;
	}

	public void setAccountActivityType(AccountActivityType accountActivityType) {
		this.accountActivityType = accountActivityType;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

}
