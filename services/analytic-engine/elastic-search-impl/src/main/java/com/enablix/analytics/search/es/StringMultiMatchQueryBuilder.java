package com.enablix.analytics.search.es;

import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

public class StringMultiMatchQueryBuilder implements MatchQueryBuilder {

	private String searchText;
	
	public StringMultiMatchQueryBuilder(String searchText) {
		this.searchText = searchText;
	}
	
	@Override
	public QueryBuilder buildQuery(String[] searchFields, MultiMatchQueryOptimizer queryOptimizer,
			FuzzyMatchOption fuzzyMatchOption) {
		
		MultiMatchQueryBuilder multiMatchQuery = QueryBuilders.multiMatchQuery(
				searchText, searchFields);
		
		if (queryOptimizer != null) {
			queryOptimizer.optimize(multiMatchQuery);
		}
		
		Fuzziness fuzziness = fuzzyMatchOption.fuzziness(searchText);
		if (fuzziness != null) {
			multiMatchQuery.fuzziness(fuzziness);
		}
		
		return multiMatchQuery;
	}

}
