package com.enablix.core.mongo.search;

import org.springframework.data.mongodb.core.query.Criteria;

public class And extends CompositeFilter {

	protected And() { }
	
	public And(SearchFilter left, SearchFilter right) {
		super(left, right);
	}

	@Override
	public Criteria toPredicate(Criteria root) {
		return root.andOperator(getLeft().toPredicate(root), getRight().toPredicate(root));
	}
	
}
