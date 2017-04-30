package com.enablix.analytics.search.es;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;

import com.enablix.analytics.search.SearchClient;
import com.enablix.core.api.ContentDataRecord;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.api.SearchResult;
import com.enablix.core.api.TemplateFacade;
import com.enablix.data.view.DataView;
import com.enablix.es.view.ESDataView;
import com.enablix.services.util.DataViewUtil;

public class ElasticSearchClient implements SearchClient {

	@Autowired
	private Client esClient;
	
	@Autowired
	private SearchHitTransformer searchHitTx;
	
	@Autowired
	private SearchFieldBuilder fieldBuilder;
	
	@Override
	public SearchResult<ContentDataRef> search(String text, 
			TemplateFacade template, int pageSize, int pageNum, DataView dataView) {

		return searchAndGetTransformedResult(text, template, pageSize, pageNum, dataView, 
						(hit, templt) -> searchHitTx.toContentDataRef(hit, templt));
	}

	private <T> SearchResult<T> searchAndGetTransformedResult(String text, TemplateFacade template,
			int pageSize, int pageNum, DataView dataView, ResultTx<T> resultTx) {
		
		SearchRequest searchRequest = buildSearchRequest(text, template, pageSize, pageNum, dataView);
		
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

	private SearchRequest buildSearchRequest(String text, 
			TemplateFacade template, int pageSize, int pageNum, DataView dataView) {
		
		ESDataView esDataView = DataViewUtil.getElasticSearchDataView(dataView);
		
		return ESQueryBuilder.builder(text, template, fieldBuilder)
													.withPagination(pageSize, pageNum)
													.withViewScope(esDataView).build();
	}
	
	@Override
	public SearchResult<ContentDataRecord> searchAndGetRecords(
			String text, TemplateFacade template, int pageSize, int pageNum, DataView dataView) {
		
		return searchAndGetTransformedResult(text, template, pageSize, pageNum, dataView, 
						(hit, templt) -> searchHitTx.toContentDataRecord(hit, templt));
	}
	
	public SearchHits searchContent(SearchRequest request) {
		ActionFuture<SearchResponse> search = esClient.search(request);
		return search.actionGet().getHits();
	}
	
	public static interface ResultTx<T> {
		T transform(SearchHit hit, TemplateFacade template);
	}

}
