package com.enablix.analytics.search.es;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enablix.core.api.TemplateFacade;
import com.enablix.es.view.ESDataView;
import com.enablix.es.view.ESDataViewOperation;
import com.enablix.services.util.ElasticSearchUtil;


public class ESQueryBuilder {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ESQueryBuilder.class);

	private String searchText;
	private TemplateFacade template;
	private int pageSize = 10;
	private int pageNum = 0;
	
	private ESDataView view;
	private SearchFieldFilter fieldFilter = SearchFieldFilter.ALL_FIELD_FILTER;
	private TypeFilter typeFilter = TypeFilter.ALL_TYPES;
	private FuzzyMatchOption fuzzyMatchOption = FuzzyMatchOption.DEFAULT_FUZZY_MATCH;

	private SearchFieldBuilder fieldBuilder;
	
	private ESQueryBuilder(String searchText, TemplateFacade template, SearchFieldBuilder fieldBuilder) {
		this.searchText = searchText;
		this.template = template;
		this.fieldBuilder = fieldBuilder;
	}
	
	public static ESQueryBuilder builder(String searchText, TemplateFacade template, SearchFieldBuilder fieldBuilder) {
		ESQueryBuilder builder = new ESQueryBuilder(searchText, template, fieldBuilder);
		return builder;
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
	
	public SearchRequest build() {
		
		String indexName = getIndexName();
		Collection<String> types = getTypes();
		
		SearchRequest searchRequest = Requests.searchRequest(indexName)
					.searchType(SearchType.DFS_QUERY_THEN_FETCH)
					.types(types.toArray(new String[0]));
		
		String[] searchFields = fieldBuilder.getContentSearchFields(fieldFilter, template);
		MultiMatchQueryBuilder multiMatchQuery = QueryBuilders.multiMatchQuery(
				searchText, searchFields);
		
		Fuzziness fuzziness = fuzzyMatchOption.fuzziness(searchText);
		if (fuzziness != null) {
			multiMatchQuery.fuzziness(fuzziness);
		}
		
		QueryBuilder searchQuery = multiMatchQuery;
		if (view != null) {
			ESDataViewOperation viewOperation = new ESDataViewOperation(view);
			searchQuery = viewOperation.createViewScopedQuery(multiMatchQuery, types);
		}
		
		//MatchQueryBuilder matchQuery = QueryBuilders.matchQuery("_all", searchText);
		SearchSourceBuilder searchSource = SearchSourceBuilder.searchSource().query(searchQuery);
		searchSource.size(pageSize);
		searchSource.from(pageNum * pageSize);
		
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
