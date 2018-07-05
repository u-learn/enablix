package com.enablix.es.core.search;

import org.elasticsearch.index.query.QueryBuilder;

public interface SearchQueryFilter {

	QueryBuilder toQueryBuilder();

}
