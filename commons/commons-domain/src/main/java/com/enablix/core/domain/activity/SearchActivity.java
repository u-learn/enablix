package com.enablix.core.domain.activity;

public class SearchActivity extends Activity {

	private String searchTerm;

	public SearchActivity(String searchTerm) {
		super(Category.SEARCH);
		this.searchTerm = searchTerm;
	}

	public String getSearchTerm() {
		return searchTerm;
	}

	public void setSearchTerm(String searchTerm) {
		this.searchTerm = searchTerm;
	}
	
}
