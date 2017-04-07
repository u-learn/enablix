package com.enablix.es.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import com.enablix.commons.util.collection.CollectionUtil;

public class ESDataViewOperation {

	private ESDataView view;

	public ESDataViewOperation(ESDataView view) {
		super();
		this.view = view;
	}
	
	public QueryBuilder createViewScopedQuery(QueryBuilder inQuery, Collection<String> searchTypes) {
		
		QueryBuilder finalQuery = inQuery;
		
		QueryBuilder viewQuery = builderViewQuery(searchTypes);
		
		if (viewQuery != null) {
			
			finalQuery = inQuery == null ? viewQuery :
				QueryBuilders.boolQuery().must(inQuery).must(viewQuery);
			
		}
		
		return finalQuery;
	}
	
	protected QueryBuilder builderViewQuery(Collection<String> searchTypes) {
		
		QueryBuilder builder = null;
		
		if (CollectionUtil.isNotEmpty(searchTypes)) {
			
			List<QueryBuilder> typeQueries = new ArrayList<>();
			
			for (String type : searchTypes) {
				
				TypeView typeView = view.typeView(type);
				
				if (typeView != null && typeView.isVisible()) {
				
					QueryBuilder baseQuery = typeView.baseQuery();
					if (baseQuery != null) {
						typeQueries.add(baseQuery);
					}
				}
			}
			
			if (CollectionUtil.isNotEmpty(typeQueries)) {
				
				BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
				
				for (QueryBuilder typeQ : typeQueries) {
					boolQuery.should(typeQ);
				}
				
				boolQuery.minimumNumberShouldMatch(1);
				builder = boolQuery;
			}

		}
		
		return builder;
	}
	
}
