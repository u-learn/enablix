package com.enablix.core.api;

import java.util.ArrayList;
import java.util.List;

public class SearchResult<T> {

	private int pageSize;
	private long currentPage;
	private long totalPages;
	private long numberOfElements;
	
	private List<T> content;
	
	public SearchResult(int pageSize, long currentPage, long totalPages, long numberOfElements, List<T> content) {
		super();
		this.pageSize = pageSize;
		this.currentPage = currentPage;
		this.totalPages = totalPages;
		this.numberOfElements = numberOfElements;
		this.content = content;
	}

	public SearchResult() {
		this.content = new ArrayList<>();
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public long getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(long currentPage) {
		this.currentPage = currentPage;
	}

	public long getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(long totalPages) {
		this.totalPages = totalPages;
	}

	public long getNumberOfElements() {
		return numberOfElements;
	}

	public void setNumberOfElements(long numberOfElements) {
		this.numberOfElements = numberOfElements;
	}

	public List<T> getContent() {
		return content;
	}

	public void setContent(List<T> content) {
		this.content = content;
	}
	
	
	
}
