package com.enablix.core.mongo.search;


public abstract class CompositeFilter extends AbstractFilter {

	private SearchFilter left;
	
	private SearchFilter right;

	protected CompositeFilter() { }
	
	public CompositeFilter(SearchFilter left, SearchFilter right) {
		this.left = left;
		this.right = right;
	}

	public SearchFilter getLeft() {
		return left;
	}

	public SearchFilter getRight() {
		return right;
	}
	
}
