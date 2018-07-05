package com.enablix.analytics.search;

import java.util.List;

import com.enablix.core.api.SearchRequest;

public class SearchInput {

	private List<String> contentQIds;
	
	private SearchRequest request;
	
	public List<String> getContentQIds() {
		return contentQIds;
	}

	public void setContentQIds(List<String> contentQIds) {
		this.contentQIds = contentQIds;
	}

	public SearchRequest getRequest() {
		return request;
	}

	public void setRequest(SearchRequest request) {
		this.request = request;
	}
	
}
