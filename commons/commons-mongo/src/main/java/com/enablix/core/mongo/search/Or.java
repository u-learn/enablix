package com.enablix.core.mongo.search;

import org.springframework.data.mongodb.core.query.Criteria;

public class Or extends CompositeFilter {

	protected Or() { }
	
	public Or(SearchFilter left, SearchFilter right) {
		super(left, right);
	}
	
	public SearchFilter or(SearchFilter orWithFilter) {
		if (orWithFilter != null) {
			if (orWithFilter instanceof Or) {
				searchFilters.addAll(((Or) orWithFilter).searchFilters);
			} else {
				searchFilters.add(orWithFilter);
			}
		}
		return this;
	}

	@Override
	protected Criteria appendFilter(Criteria root, Criteria[] filterCriteria) {
		return root.orOperator(filterCriteria);
	}
	
}
