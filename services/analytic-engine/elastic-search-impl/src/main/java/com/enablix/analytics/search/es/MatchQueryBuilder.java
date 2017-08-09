package com.enablix.analytics.search.es;

import org.elasticsearch.index.query.QueryBuilder;

public interface MatchQueryBuilder {

	QueryBuilder buildQuery(String[] searchFields, MultiMatchQueryOptimizer queryOptimizer,
			FuzzyMatchOption fuzzyMatchOption);

}
