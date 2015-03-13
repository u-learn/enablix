package com.enablix.core.mongo.search;

import org.springframework.data.mongodb.core.query.Criteria;

public class SearchCriteria implements SearchFilter {

	private SearchFilter root;
	
	protected SearchCriteria() { }
	
	public SearchCriteria(SearchFilter root) {
		setRootFilter(root);
	}
	
	@Override
	public Criteria toPredicate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SearchFilter and(SearchFilter andWithFilter) {
		root = root.and(andWithFilter);
		return this;
	}

	@Override
	public SearchFilter or(SearchFilter orWithFilter) {
		root = root.or(orWithFilter);
		return this;
	}

	protected void setRootFilter(SearchFilter root) {
		this.root = root;
	}
	
}
