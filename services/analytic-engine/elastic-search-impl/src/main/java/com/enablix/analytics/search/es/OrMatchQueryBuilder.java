package com.enablix.analytics.search.es;

import java.util.List;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

public class OrMatchQueryBuilder implements MatchQueryBuilder {

	private List<String> searchTexts;
	
	public OrMatchQueryBuilder(List<String> searchTexts) {
		super();
		this.searchTexts = searchTexts;
	}


	@Override
	public QueryBuilder buildQuery(String[] searchFields, MultiMatchQueryOptimizer queryOptimizer,
			FuzzyMatchOption fuzzyMatchOption) {
		
		if (searchTexts.size() == 1) {
			return getStringQueryBuilder(searchTexts.get(0), searchFields, queryOptimizer, fuzzyMatchOption);
		}
		
		BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
		boolQuery.minimumNumberShouldMatch(1);
		
		searchTexts.forEach((searchTxt) -> {
			boolQuery.should(getStringQueryBuilder(searchTxt, searchFields, queryOptimizer, fuzzyMatchOption));
		});
		
		return boolQuery;
	}

	private QueryBuilder getStringQueryBuilder(String searchText, String[] searchFields, 
			MultiMatchQueryOptimizer queryOptimizer, FuzzyMatchOption fuzzyMatchOption) {
		return new StringMultiMatchQueryBuilder(searchText).buildQuery(searchFields, queryOptimizer, fuzzyMatchOption);
	}
	
	
}
