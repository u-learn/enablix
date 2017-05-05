package com.enablix.analytics.search.es;

import org.elasticsearch.index.query.MultiMatchQueryBuilder;

public interface MultiMatchQueryOptimizer {

	void optimize(MultiMatchQueryBuilder multiMatchQuery);
	
}

