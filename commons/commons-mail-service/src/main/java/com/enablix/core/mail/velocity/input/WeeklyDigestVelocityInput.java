package com.enablix.core.mail.velocity.input;

import java.util.HashMap;
import java.util.List;

import com.enablix.app.content.ui.NavigableContent;
import com.enablix.core.domain.security.authorization.UserProfile;
import com.enablix.core.domain.user.User;

public class WeeklyDigestVelocityInput extends BaseVelocityInput implements  LoggedInUserAware, EnvPropertiesAware{

	private HashMap<String, List<NavigableContent>> recentList ;
	private HashMap<String,List<HashMap<String,Object>>> sideBarItems; 
	private String url;
	private UserProfile recipientUser;
	private String identity; // unique identifier for weekly digest

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String weeklyDigestId) {
		this.identity = weeklyDigestId;
	}

	public HashMap<String, List<NavigableContent>> getRecentList() {
		return recentList;
	}

	public void setRecentList(String key, List<NavigableContent> recentList) {
		
		this.recentList = new HashMap<String,List<NavigableContent>>();
		this.recentList.put(key, recentList);		
	}

	public HashMap<String, List<HashMap<String, Object>>> getSideBarItems() {
		return sideBarItems;
	}

	public void setSideBarItems(HashMap<String, List<HashMap<String, Object>>> sideBarItems) {
		this.sideBarItems = sideBarItems;
	}

	public void setRecentList(HashMap<String, List<NavigableContent>> recentList) {
		this.recentList = recentList;
	}

	@Override
	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String getUrl() {
		return url;
	}
	
	@Override
	public UserProfile getLoggedInUser() {
		return recipientUser;
	}
	
	@Override
	public void setLoggedInUser(UserProfile loggedInUser) {
		this.recipientUser = loggedInUser;
	}

	public UserProfile getRecipientUser() {
		return recipientUser;
	}

	public void setRecipientUser(UserProfile recepientUser) {
		this.recipientUser = recepientUser;
	}
		
}
