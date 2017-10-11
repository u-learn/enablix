package com.enablix.app.web;

import java.util.List;

import com.enablix.app.web.LinkedContentController.LinkedContent;

public class HubspotCRMExtResponse {

	private List<LinkedContent> results;
	
	private int totalCount;
	
	private String allItemsLink;
	
	private String itemLabel;

	public List<LinkedContent> getResults() {
		return results;
	}

	public void setResults(List<LinkedContent> results) {
		this.results = results;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public String getAllItemsLink() {
		return allItemsLink;
	}

	public void setAllItemsLink(String allItemsLink) {
		this.allItemsLink = allItemsLink;
	}

	public String getItemLabel() {
		return itemLabel;
	}

	public void setItemLabel(String itemLabel) {
		this.itemLabel = itemLabel;
	}
	
	
}
