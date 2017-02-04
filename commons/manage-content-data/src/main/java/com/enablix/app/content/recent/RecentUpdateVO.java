package com.enablix.app.content.recent;

import com.enablix.app.content.ui.NavigableContent;
import com.enablix.core.domain.recent.RecentData;

public class RecentUpdateVO {

	private RecentData recentData;

	private NavigableContent recentNavContent;
	
	public RecentUpdateVO(RecentData recentData, NavigableContent recentNavContent) {
		super();
		this.recentData = recentData;
		this.recentNavContent = recentNavContent;
	}

	public RecentData getRecentData() {
		return recentData;
	}

	public void setRecentData(RecentData recentData) {
		this.recentData = recentData;
	}

	public NavigableContent getRecentNavContent() {
		return recentNavContent;
	}

	public void setRecentNavContent(NavigableContent recentNavContent) {
		this.recentNavContent = recentNavContent;
	}
	
}
