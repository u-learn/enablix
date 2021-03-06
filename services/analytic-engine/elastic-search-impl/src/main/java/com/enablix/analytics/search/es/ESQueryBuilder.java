package com.enablix.analytics.search.es;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Requests;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AbstractAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.highlight.HighlightBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.core.api.TemplateFacade;
import com.enablix.es.view.ESDataView;
import com.enablix.es.view.ESDataViewOperation;
import com.enablix.services.util.ElasticSearchUtil;


public class ESQueryBuilder {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ESQueryBuilder.class);

	private TemplateFacade template;
	private int pageSize = 10;
	private int pageNum = 0;
	
	private ESDataView view;
	private SearchFieldFilter fieldFilter = SearchFieldFilter.ALL_FIELD_FILTER;
	private TypeFilter typeFilter = TypeFilter.ALL_TYPES;
	private FuzzyMatchOption fuzzyMatchOption = FuzzyMatchOption.DEFAULT_FUZZY_MATCH;
	private MultiMatchQueryOptimizer queryOptimizer;
	private HighlightBuilder highlightBuilder;
	private SearchFieldBuilder fieldBuilder;

	private AbstractAggregationBuilder aggregation;
	
	private MatchQueryBuilder matchQueryBuilder;
	
	private ESQueryBuilder(MatchQueryBuilder matchQueryBuilder, TemplateFacade template, SearchFieldBuilder fieldBuilder) {
		this.template = template;
		this.fieldBuilder = fieldBuilder;
		this.matchQueryBuilder = matchQueryBuilder;
	}
	
	public static ESQueryBuilder builder(String searchText, TemplateFacade template, SearchFieldBuilder fieldBuilder) {
		return builder(new StringMultiMatchQueryBuilder(searchText), template, fieldBuilder);
	}

	public static ESQueryBuilder builder(MatchQueryBuilder matchQueryBuilder, TemplateFacade template, SearchFieldBuilder fieldBuilder) {
		return new ESQueryBuilder(matchQueryBuilder, template, fieldBuilder);
	}

	
	public ESQueryBuilder withPagination(int pageSize, int pageNum) {
		this.pageNum = pageNum;
		this.pageSize = pageSize;
		return this;
	}
	
	public ESQueryBuilder withViewScope(ESDataView view) {
		this.view = view;
		return this;
	}
	
	public ESQueryBuilder withFieldFilter(SearchFieldFilter filter) {
		this.fieldFilter = filter;
		return this;
	}
	
	public ESQueryBuilder withTypeFilter(TypeFilter filter) {
		this.typeFilter = filter;
		return this;
	}
	
	public ESQueryBuilder withFuzziness(FuzzyMatchOption fuzzyMatchOption) {
		this.fuzzyMatchOption = fuzzyMatchOption;
		return this;
	}
	
	public ESQueryBuilder withOptimizer(MultiMatchQueryOptimizer optimizer) {
		this.queryOptimizer = optimizer;
		return this;
	}
	
	public ESQueryBuilder withAggregation(AbstractAggregationBuilder agg) {
		this.aggregation = agg;
		return this;
	}
	
	public ESQueryBuilder withHighlighter(HighlightBuilder highlighter) {
		this.highlightBuilder = highlighter;
		return this;
	}
	
	public SearchRequest build() {
		
		String indexName = getIndexName();
		Collection<String> types = getTypes();
		
		SearchRequest searchRequest = Requests.searchRequest(indexName)
					.searchType(SearchType.DFS_QUERY_THEN_FETCH)
					.types(types.toArray(new String[0]));
		
		String[] searchFields = fieldBuilder.getContentSearchFields(fieldFilter, template);
		QueryBuilder multiMatchQuery = matchQueryBuilder.buildQuery(searchFields, queryOptimizer, fuzzyMatchOption);
		
		BoolQueryBuilder archivedQueryBuilder = QueryBuilders.boolQuery().must(
				QueryBuilders.matchQuery(ContentDataConstants.ARCHIVED_KEY, false).boost(10));
		
		BoolQueryBuilder baseQuery = QueryBuilders.boolQuery()
				.must(multiMatchQuery)
				.should(archivedQueryBuilder);
		
		QueryBuilder searchQuery = baseQuery;
		if (view != null) {
			ESDataViewOperation viewOperation = new ESDataViewOperation(view);
			searchQuery = viewOperation.createViewScopedQuery(baseQuery, types);
		}
		
		//MatchQueryBuilder matchQuery = QueryBuilders.matchQuery("_all", searchText);
		SearchSourceBuilder searchSource = SearchSourceBuilder.searchSource().query(searchQuery);
		searchSource.size(pageSize);
		searchSource.from(pageNum * pageSize);

		if (aggregation != null) {
			searchSource.aggregation(aggregation);
		}
		
		if (this.highlightBuilder != null) {
			searchSource.highlight(highlightBuilder);
		}
		
		searchRequest.source(searchSource);
		
		if (LOGGER.isDebugEnabled()) {
			
			LOGGER.debug("Search index: {}", indexName);
			LOGGER.debug("Search types: {}", types);
			LOGGER.debug("Search query: {}", searchSource);
		}
		
		return searchRequest;
	}
	
	private String getIndexName() {
		return ElasticSearchUtil.getIndexName();
	}
	
	private Collection<String> getTypes() {
		
		Collection<String> allTypes = template.getAllCollectionNames();
		
		if (view != null) {
			allTypes = view.checkAndReturnVisibleTypes(allTypes);
		}
		
		if (typeFilter != null && typeFilter != TypeFilter.ALL_TYPES) {
			
			List<String> filteredList = new ArrayList<>();
			
			for (String type : allTypes) {
				if (typeFilter.searchInType(type)) {
					filteredList.add(type);
				}
			}
			
			allTypes = filteredList;
		}
		
		return allTypes;
	}
	
}
