package com.enablix.core.mongo.search;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.query.Criteria;

public class And extends CompositeFilter {

	private List<SearchFilter> searchFilters;
	
	protected And() { }
	
	public And(SearchFilter left, SearchFilter right) {
		this.searchFilters = new ArrayList<>();
		searchFilters.add(left);
		searchFilters.add(right);
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
	public Criteria toPredicate(Criteria root) {
		int indx = 0;
		Criteria[] criteriaList = new Criteria[searchFilters.size()];
		for (SearchFilter filter : searchFilters) {
			criteriaList[indx++] = filter.toPredicate(root);
		}
		return root.andOperator(criteriaList);
	}
	
}
