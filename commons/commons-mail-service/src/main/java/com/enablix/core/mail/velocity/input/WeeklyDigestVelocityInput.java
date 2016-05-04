package com.enablix.core.mail.velocity.input;

import java.util.HashMap;
import java.util.List;

import com.enablix.app.content.ui.NavigableContent;

public class WeeklyDigestVelocityInput extends BaseVelocityInput{

	private HashMap<String, List<NavigableContent>> recentList ;
	private HashMap<String,List<HashMap<String,Object>>> sideBarItems; 

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

		
}
