package com.enablix.analytics.search.es;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import com.enablix.analytics.search.SearchClient;
import com.enablix.analytics.search.SearchScope;
import com.enablix.commons.util.StringUtil;
import com.enablix.core.api.ContentDataRecord;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.api.SearchResult;
import com.enablix.core.api.TemplateFacade;
import com.enablix.data.view.DataView;
import com.enablix.es.view.ESDataView;
import com.enablix.services.util.DataViewUtil;

public class ElasticSearchClient implements SearchClient {

	public static interface ResultTx<T> {
		T transform(SearchHit hit, TemplateFacade template);
	}
	
	@Autowired
	private Client esClient;
	
	@Autowired
	private SearchHitTransformer searchHitTx;
	
	private SearchFieldBuilder defaultSearchFieldBuilder = new DefaultSearchFieldBuilder();
	
	private TypeaheadSearchFieldBuilder typeaheadSearchFieldBuilder = new TypeaheadSearchFieldBuilder();
	
	@Override
	public SearchResult<ContentDataRef> search(String text, SearchScope scope,
			TemplateFacade template, int pageSize, int pageNum, DataView dataView) {

		return searchAndGetTransformedResult(text, scope, template, pageSize, pageNum, dataView, 
						(hit, templt) -> searchHitTx.toContentDataRef(hit, templt));
	}

	private <T> SearchResult<T> searchAndGetTransformedResult(String text, SearchScope scope,
			TemplateFacade template, int pageSize, int pageNum, DataView dataView, ResultTx<T> resultTx) {
		
		SearchRequest searchRequest = buildSearchRequest(text, scope, template, pageSize, pageNum, dataView);
		
		return executeSearchAndCreateResult(template, pageSize, pageNum, resultTx, searchRequest);
	}

	private <T> SearchResult<T> executeSearchAndCreateResult(TemplateFacade template, int pageSize, int pageNum,
			ResultTx<T> resultTx, SearchRequest searchRequest) {

		ActionFuture<SearchResponse> searchResponseFuture = esClient.search(searchRequest);
		SearchResponse searchResponse = searchResponseFuture.actionGet();
		
		List<T> result = new ArrayList<>();
		
		SearchHits hits = searchResponse.getHits();
		for (SearchHit hit : hits) {
			T txHit = resultTx.transform(hit, template); //searchHitTx.toContentDataRef(hit, template);
			if (txHit != null) {
				result.add(txHit);
			}
		}
		
		return createSearchResult(pageSize, pageNum, result, hits.getTotalHits());
	}

	private <T> SearchResult<T> createSearchResult(int pageSize, int pageNum, List<T> result, long totalHits) {
		
		SearchResult<T> searchResult = new SearchResult<>();
		
		searchResult.setContent(result);
		searchResult.setNumberOfElements(totalHits);
		searchResult.setPageSize(pageSize);
		searchResult.setCurrentPage(pageNum);
		searchResult.setTotalPages((long) Math.ceil(((double) totalHits)/pageSize));
		
		return searchResult;
	}

	private SearchRequest buildSearchRequest(String text, SearchScope scope,
			TemplateFacade template, int pageSize, int pageNum, DataView dataView) {
		
		ElasticSearchScope esScope = new ElasticSearchScope(scope, template);
		ESDataView esDataView = DataViewUtil.getElasticSearchDataView(dataView);
		
		return ESQueryBuilder.builder(text, template, defaultSearchFieldBuilder)
							 .withPagination(pageSize, pageNum)
							 .withTypeFilter(esScope.getTypeFilter())
							 .withViewScope(esDataView).build();
	}
	
	@Override
	public SearchResult<ContentDataRecord> searchAndGetRecords(String text, SearchScope scope, 
			TemplateFacade template, int pageSize, int pageNum, DataView dataView) {
		
		return searchAndGetTransformedResult(text, scope, template, pageSize, pageNum, dataView, 
						(hit, templt) -> searchHitTx.toContentDataRecord(hit, templt));
	}
	
	public SearchHits searchContent(SearchRequest request) {
		ActionFuture<SearchResponse> search = esClient.search(request);
		return search.actionGet().getHits();
	}
	
	public ActionFuture<SearchResponse> executeSearch(SearchRequest request) {
		return esClient.search(request);
	}
	
	@Override
	public SearchResult<ContentDataRecord> searchBizContentRecords(String text, 
			SearchScope scope, TemplateFacade template, int pageSize, int pageNum, DataView dataView) {

		return executeSearchAndCreateResult(template, pageSize, pageNum, 
				(hit, templt) -> searchHitTx.toContentDataRecord(hit, templt),
				buildBizContentSearchRequest(text, scope, template, pageSize, pageNum, dataView));
	}
	
	@Override
	public SearchResult<ContentDataRecord> searchAsYouTypeBizContentRecords(String text, 
			SearchScope scope, TemplateFacade template, int pageSize, int pageNum, DataView dataView) {
		
		return executeSearchAndCreateResult(template, pageSize, pageNum, 
				(hit, templt) -> searchHitTx.toContentDataRecord(hit, templt),
				buildTypeaheadBizContentSearchRequest(text, scope, template, pageSize, pageNum, dataView));
	}
	
	private SearchRequest buildTypeaheadBizContentSearchRequest(String text, 
			SearchScope scope, TemplateFacade template, int pageSize, int pageNum, DataView dataView) {
		
		ElasticSearchScope esScope = new ElasticSearchScope(scope, template);
		ESDataView esDataView = DataViewUtil.getElasticSearchDataView(dataView);
		
		TypeFilter typeFilter = esScope.getTypeFilter();
		if (typeFilter == null) {
			typeFilter = getBizContentTypeFilter(template);
		}

		HighlightBuilder highlighter = new HighlightBuilder();
		for (String field : typeaheadSearchFieldBuilder.getFields()) {
			highlighter.field(field);
		}

		QueryStringMatchQueryBuilder matchQueryBuilder = new QueryStringMatchQueryBuilder("*" + text.trim() + "*");
		return ESQueryBuilder.builder(matchQueryBuilder, template, typeaheadSearchFieldBuilder)
				.withPagination(pageSize, pageNum)
				.withFuzziness((searchTerm) -> null)
				.withTypeFilter(typeFilter)
				.withHighlighter(highlighter)
				.withViewScope(esDataView)
				.build();
	}
	
	private SearchRequest buildBizContentSearchRequest(String text, SearchScope scope,
			TemplateFacade template, int pageSize, int pageNum, DataView dataView) {
		
		ElasticSearchScope esScope = new ElasticSearchScope(scope, template);
		ESDataView esDataView = DataViewUtil.getElasticSearchDataView(dataView);
		
		TypeFilter typeFilter = esScope.getTypeFilter();
		if (typeFilter == null) {
			typeFilter = getBizContentTypeFilter(template);
		}
		
		return ESQueryBuilder.builder(text, template, defaultSearchFieldBuilder)
				.withPagination(pageSize, pageNum)
				.withTypeFilter(typeFilter)
				.withViewScope(esDataView)
				.build();
		
	}
	
	private TypeFilter getBizContentTypeFilter(TemplateFacade template) {
		
		Collection<String> bizContentCollections = new HashSet<>();
		
		template.getBizContentContainers().forEach((container) -> {
			String collectionName = template.getCollectionName(container.getQualifiedId());
			if (StringUtil.hasText(collectionName)) {
				bizContentCollections.add(collectionName);
			}
		});
		
		return (searchType) -> bizContentCollections.contains(searchType);
	}

}
