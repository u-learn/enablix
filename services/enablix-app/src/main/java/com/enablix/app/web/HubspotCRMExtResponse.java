package com.enablix.app.web;

import java.util.List;

public class HubspotCRMExtResponse {

	private List<HubspotContent> results;
	
	private int totalCount;
	
	private String allItemsLink = "All Content";
	
	private String itemLabel;

	public List<HubspotContent> getResults() {
		return results;
	}

	public void setResults(List<HubspotContent> results) {
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
	
	
	public static class HubspotContent {
		
		private String contentLabel;
		
		private int contentCount;

		public String getContentLabel() {
			return contentLabel;
		}

		public void setContentLabel(String contentLabel) {
			this.contentLabel = contentLabel;
		}

		public int getContentCount() {
			return contentCount;
		}

		public void setContentCount(int contentCount) {
			this.contentCount = contentCount;
		}
		
	}
	
}
