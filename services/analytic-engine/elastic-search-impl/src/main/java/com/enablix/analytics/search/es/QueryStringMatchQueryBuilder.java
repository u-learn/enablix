package com.enablix.analytics.search.es;

import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;

public class QueryStringMatchQueryBuilder implements MatchQueryBuilder {

	private String queryString;
	
	public QueryStringMatchQueryBuilder(String queryString) {
		super();
		this.queryString = queryString;
	}

	@Override
	public QueryBuilder buildQuery(String[] searchFields, MultiMatchQueryOptimizer queryOptimizer,
			FuzzyMatchOption fuzzyMatchOption) {
		
		QueryStringQueryBuilder queryBuilder = QueryBuilders.queryStringQuery(queryString);
		for (String field: searchFields) {
			queryBuilder.field(field);
		}
		
		Fuzziness fuzziness = fuzzyMatchOption.fuzziness(queryString);
		if (fuzziness != null) {
			queryBuilder.fuzziness(fuzziness);
		}
		
		return queryBuilder;
	}

}
