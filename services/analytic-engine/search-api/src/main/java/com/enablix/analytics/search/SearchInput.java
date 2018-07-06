package com.enablix.analytics.search;

import java.util.List;

import com.enablix.core.api.SearchRequest;

public class SearchInput {

	private List<String> contentQIds;
	
	private ParentFilter parent;
	
	private SearchRequest request;
	
	public List<String> getContentQIds() {
		return contentQIds;
	}

	public void setContentQIds(List<String> contentQIds) {
		this.contentQIds = contentQIds;
	}

	public ParentFilter getParent() {
		return parent;
	}

	public void setParent(ParentFilter parent) {
		this.parent = parent;
	}

	public SearchRequest getRequest() {
		return request;
	}

	public void setRequest(SearchRequest request) {
		this.request = request;
	}
	
	public static class ParentFilter {
		
		private String qualifiedId;
		
		private String identity;

		public String getQualifiedId() {
			return qualifiedId;
		}

		public void setQualifiedId(String qualifiedId) {
			this.qualifiedId = qualifiedId;
		}

		public String getIdentity() {
			return identity;
		}

		public void setIdentity(String identity) {
			this.identity = identity;
		}
		
	}
	
}
