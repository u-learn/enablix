package com.enablix.core.mongo.search;

import org.springframework.data.mongodb.core.query.Criteria;

public class And extends CompositeFilter {

	protected And() { }
	
	public And(SearchFilter left, SearchFilter right) {
		super(left, right);
	}

	public SearchFilter and(SearchFilter andWithFilter) {
		if (andWithFilter != null) {
			if (andWithFilter instanceof And) {
				searchFilters.addAll(((And) andWithFilter).searchFilters);
			} else {
				searchFilters.add(andWithFilter);
			}
		}
		return this;
	}

	@Override
	protected Criteria appendFilter(Criteria root, Criteria[] filterCriteria) {
		return root.andOperator(filterCriteria);
	}
	
}
