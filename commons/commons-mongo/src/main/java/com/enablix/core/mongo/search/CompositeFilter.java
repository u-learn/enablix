package com.enablix.core.mongo.search;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.query.Criteria;

public abstract class CompositeFilter extends AbstractFilter {

	protected List<SearchFilter> searchFilters;

	protected CompositeFilter() { }
	
	public CompositeFilter(SearchFilter left, SearchFilter right) {
		this.searchFilters = new ArrayList<>();
		searchFilters.add(left);
		searchFilters.add(right);
	}
	
	@Override
	public Criteria toPredicate(Criteria root) {
		int indx = 0;
		Criteria[] criteriaList = new Criteria[searchFilters.size()];
		for (SearchFilter filter : searchFilters) {
			criteriaList[indx++] = filter.toPredicate(new Criteria());
		}
		return appendFilter(root, criteriaList);
	}
	
	protected abstract Criteria appendFilter(Criteria root, Criteria[] filterCriteria);

}
