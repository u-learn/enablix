package com.enablix.core.mongo.search;

import org.springframework.data.mongodb.core.query.Criteria;

public class Or extends CompositeFilter {

	protected Or() { }
	
	public Or(SearchFilter left, SearchFilter right) {
		super(left, right);
	}

	@Override
	public Criteria toPredicate() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
