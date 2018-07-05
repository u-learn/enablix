package com.enablix.analytics.search.es;

import java.util.List;
import java.util.Map;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.core.api.SearchRequest;
import com.enablix.core.api.SearchRequest.FilterMetadata;
import com.enablix.es.core.search.BoolFilter;
import com.enablix.es.core.search.NumericFilter;
import com.enablix.es.core.search.SearchQueryFilter;
import com.enablix.es.core.search.StringFilter;
import com.enablix.es.core.search.StringListFilter;

public class SearchRequestQueryBuilder implements MatchQueryBuilder {

	private SearchRequest request;
	private MatchQueryBuilder stringMatchBuilder;
	
	public SearchRequestQueryBuilder(SearchRequest searchRequest) {
		this(searchRequest, null);
	}
	
	public SearchRequestQueryBuilder(SearchRequest request, MatchQueryBuilder stringMatchBuilder) {
		
		super();
		
		this.request = request;
		this.stringMatchBuilder = stringMatchBuilder;
		
		if (stringMatchBuilder == null) {
			this.stringMatchBuilder = new StringMultiMatchQueryBuilder(request.getTextQuery());
		}
	}


	@Override
	public QueryBuilder buildQuery(String[] searchFields, MultiMatchQueryOptimizer queryOptimizer,
			FuzzyMatchOption fuzzyMatchOption) {
		
		QueryBuilder textQuery = stringMatchBuilder.buildQuery(searchFields, queryOptimizer, fuzzyMatchOption);
		
		BoolQueryBuilder boolQuery = null;
		
		if (CollectionUtil.isNotEmpty(request.getFilters())) {
			
			boolQuery = QueryBuilders.boolQuery();
			boolQuery.must(textQuery);
		
			Map<String, FilterMetadata> filterMetadata = request.getFilterMetadata();
			
			for (Map.Entry<String, Object> entry : request.getFilters().entrySet()) {
				
				FilterMetadata metadata = filterMetadata.get(entry.getKey());
				
				if (metadata == null) {
					metadata = new FilterMetadata();
					metadata.setField(entry.getKey());
				}
				
				QueryBuilder queryBuilder = buildSearchFilter(metadata, entry.getValue());
				
				if (queryBuilder != null) {
					boolQuery.filter(queryBuilder);
				}
			}
			
		}
		
		return boolQuery != null ? boolQuery : textQuery;
		
	}
	
	@SuppressWarnings("unchecked")
	private QueryBuilder buildSearchFilter(FilterMetadata metadata, Object filterValue) {
		
		SearchQueryFilter filter = null;
		
		switch (metadata.getDataType()) {
			
			case DATE:
				throw new UnsupportedOperationException("Date filter not supported for text query search");
				
			case BOOL:
				Boolean propValue = filterValue instanceof Boolean ? (Boolean) filterValue : 
										Boolean.valueOf(String.valueOf(filterValue));
				filter = new BoolFilter(metadata.getField(), propValue, metadata.getOperator());
				break;
				
			case NUMBER:
				
				Number numValue = null;
				
				if (filterValue instanceof Number) {
					numValue = (Number) filterValue;
				} else {
					numValue = Double.parseDouble(String.valueOf(filterValue));
				}
				
				filter = new NumericFilter(metadata.getField(), numValue, metadata.getOperator());
				break;
				
			case STRING:
				
				if (filterValue instanceof List) {
					filter = new StringListFilter(metadata.getField(), (List<String>) filterValue, metadata.getOperator());
					break;
				}
				
			default:
				filter = new StringFilter(metadata.getField(), String.valueOf(filterValue), metadata.getOperator());
				break;
		}
		
		return filter != null ? filter.toQueryBuilder() : null;
	}

}
