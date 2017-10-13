package com.enablix.hubspot.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class HubspotCRMExtResponse {

	private List<HubspotContent> results;
	
	private int totalCount;
	
	private String allItemsLink;
	
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
	
}
