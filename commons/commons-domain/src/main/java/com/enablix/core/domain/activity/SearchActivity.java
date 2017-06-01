package com.enablix.core.domain.activity;

public class SearchActivity extends Activity {

	private String searchTerm;
	
	private long pageNum;
	
	private long resultCount;
	
	public SearchActivity(String searchTerm, long pageNum, long resultCount) {
		super(Category.SEARCH);
		this.searchTerm = searchTerm;
		this.pageNum = pageNum;
		this.resultCount = resultCount;
	}

	public String getSearchTerm() {
		return searchTerm;
	}

	public void setSearchTerm(String searchTerm) {
		this.searchTerm = searchTerm;
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
