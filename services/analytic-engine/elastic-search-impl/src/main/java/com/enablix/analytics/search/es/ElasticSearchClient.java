package com.enablix.analytics.search.es;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.enablix.analytics.search.SearchClient;
import com.enablix.analytics.search.SearchInput;
import com.enablix.commons.constants.AppConstants;
import com.enablix.commons.util.StringUtil;
import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.core.api.ContentDataRecord;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.api.SearchRequest.Pagination;
import com.enablix.core.api.SearchResult;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.ContainerType;
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
	public SearchResult<ContentDataRef> search(String text, SearchInput scope,
			TemplateFacade template, int pageSize, int pageNum, DataView dataView) {

		return searchAndGetTransformedResult(text, scope, template, pageSize, pageNum, dataView, 
						(hit, templt) -> searchHitTx.toContentDataRef(hit, templt));
	}

	private <T> SearchResult<T> searchAndGetTransformedResult(String text, SearchInput scope,
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

	private SearchRequest buildSearchRequest(String text, SearchInput scope,
			TemplateFacade template, int pageSize, int pageNum, DataView dataView) {
		
		ElasticSearchScope esScope = new ElasticSearchScope(scope, template);
		ESDataView esDataView = DataViewUtil.getElasticSearchDataView(dataView);
		
		return ESQueryBuilder.builder(text, template, defaultSearchFieldBuilder)
							 .withPagination(pageSize, pageNum)
							 .withTypeFilter(esScope.getTypeFilter())
							 .withViewScope(esDataView).build();
	}
	
	@Override
	public SearchResult<ContentDataRecord> searchAndGetRecords(String text, SearchInput scope, 
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
	public SearchResult<ContentDataRecord> searchBizContentRecords(
			SearchInput input, TemplateFacade template, DataView dataView) {

		int pageSize = input.getRequest().getPagination().getPageSize();
		int pageNum = input.getRequest().getPagination().getPageNum();
		
		return executeSearchAndCreateResult(template, pageSize, pageNum, 
				(hit, templt) -> searchHitTx.toContentDataRecord(hit, templt),
				buildBizContentSearchRequest(input, template, dataView));
	}
	
	@Override
	public SearchResult<ContentDataRecord> searchAsYouTypeBizContentRecords( 
			SearchInput scope, TemplateFacade template, DataView dataView) {
		
		int pageNum = scope.getRequest().getPagination().getPageNum();
		int pageSize = scope.getRequest().getPagination().getPageSize();
		
		return executeSearchAndCreateResult(template, pageSize, pageNum, 
				(hit, templt) -> searchHitTx.toContentDataRecord(hit, templt),
				buildTypeaheadBizContentSearchRequest(scope, template, dataView));
	}
	
	private SearchRequest buildTypeaheadBizContentSearchRequest( 
			SearchInput input, TemplateFacade template, DataView dataView) {
		
		ElasticSearchScope esScope = new ElasticSearchScope(input, template);
		ESDataView esDataView = DataViewUtil.getElasticSearchDataView(dataView);
		
		TypeFilter typeFilter = esScope.getTypeFilter();
		if (typeFilter == null) {
			typeFilter = getBizContentTypeFilter(template);
		}

		HighlightBuilder highlighter = new HighlightBuilder();
		for (String field : typeaheadSearchFieldBuilder.getFields()) {
			highlighter.field(field);
		}

		String text = input.getRequest().getTextQuery();
		int pageSize = input.getRequest().getPagination().getPageSize();
		int pageNum = input.getRequest().getPagination().getPageNum();
		
		QueryStringMatchQueryBuilder stringMatchBuilder = new QueryStringMatchQueryBuilder("*" + text.trim() + "*");
		SearchRequestQueryBuilder matchQueryBuilder = new SearchRequestQueryBuilder(input, template, stringMatchBuilder);
		
		return ESQueryBuilder.builder(matchQueryBuilder, template, typeaheadSearchFieldBuilder)
				.withPagination(pageSize, pageNum)
				.withFuzziness((searchTerm) -> null)
				.withTypeFilter(typeFilter)
				.withHighlighter(highlighter)
				.withViewScope(esDataView)
				.build();
	}
	
	private SearchRequest buildBizContentSearchRequest(SearchInput scope,
			TemplateFacade template, DataView dataView) {
		
		Collection<String> qIds = scope.getContentQIds();

		if (CollectionUtil.isEmpty(qIds)) {
			qIds = template.getBizContentContainers().stream()
					.map(ContainerType::getQualifiedId).collect(Collectors.toList());
		}
		
		Collection<String> typenames = getContentTypenames(template, qIds);
		
		return buildSearchRequestQuery(typenames, template, scope, null, dataView);
		
	}

	private Collection<String> getContentTypenames(TemplateFacade template, Collection<String> qIds) {
		
		Collection<String> typenames = new HashSet<>();
		
		qIds.forEach((qId) -> {
			String collectionName = template.getCollectionName(qId);
			if (StringUtil.hasText(collectionName)) {
				typenames.add(collectionName);
			}
		});
		
		return typenames;
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

	@Override
	public Page<?> searchTypeRecords(SearchInput input, TemplateFacade template, DataView userView) {
		
		Page<?> result = null;
		
		Collection<String> contentTypenames = getContentTypenames(template, input.getContentQIds());
		
		if (CollectionUtil.isNotEmpty(contentTypenames)) {
			
			Pagination pagination = input.getRequest().getPagination();
			int pageNum = pagination != null ? pagination.getPageNum() : 0;
			int pageSize = pagination != null ? pagination.getPageSize() : AppConstants.DEFAULT_PAGE_SIZE;
			
			SearchResult<Map<String, Object>> searchResult = 
				executeSearchAndCreateResult(template, pageSize, pageNum, 
					(hit, tmplt) -> hit.getSource(), 
					buildSearchRequestQuery(contentTypenames, 
							template, input, null, userView));
			
			result = new PageImpl<>(searchResult.getContent(), 
					new PageRequest(pageNum, pageSize), 
					searchResult.getNumberOfElements());
			
		} else {
			result = new PageImpl<>(new ArrayList<>());
		}
		
		return result;
	}

	private SearchRequest buildSearchRequestQuery(Collection<String> typenames, TemplateFacade template,
			SearchInput searchInput, MatchQueryBuilder stringMatchBuilder, DataView userView) {
		
		ESDataView esView = DataViewUtil.getElasticSearchDataView(userView);
		SearchRequestQueryBuilder matchQueryBuilder = new SearchRequestQueryBuilder(searchInput, template, stringMatchBuilder);
		
		com.enablix.core.api.SearchRequest searchRequest = searchInput.getRequest();
		int pageSize = searchRequest.getPagination().getPageSize();
		int pageNum = searchRequest.getPagination().getPageNum();
		
		return ESQueryBuilder.builder(matchQueryBuilder, template, defaultSearchFieldBuilder)
							 .withTypeFilter((searchType) -> typenames.contains(searchType))
							 .withViewScope(esView)
							 .withPagination(pageSize, pageNum)
							 .build();
	}

}
