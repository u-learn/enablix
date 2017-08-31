package com.enablix.core.domain.activity;

public class SearchActivity extends BaseSearchActivity {

	private long pageNum;
	
	private long resultCount;
	
	@SuppressWarnings("unused")
	private SearchActivity() {
		// for ORM
	}
	
	public SearchActivity(String searchTerm, long pageNum, long resultCount) {
		super(ActivityType.SEARCH_FREE_TEXT, searchTerm);
		this.pageNum = pageNum;
		this.resultCount = resultCount;
	}

	public long getPageNum() {
		return pageNum;
	}

	public void setPageNum(long pageNum) {
		this.pageNum = pageNum;
	}

	public long getResultCount() {
		return resultCount;
	}

	public void setResultCount(long resultCount) {
		this.resultCount = resultCount;
	}
	
}
