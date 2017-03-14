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
import com.enablix.services.util.template.TemplateWrapper;

public class ElasticSearchClient implements SearchClient {

	@Autowired
	private Client esClient;
	
	@Autowired
	private SearchHitTransformer searchHitTx;
	
	@Override
	public SearchResult<ContentDataRef> search(String text, TemplateWrapper template, int pageSize, int pageNum) {

		return searchAndGetTransformedResult(text, template, pageSize, pageNum, new ResultTx<ContentDataRef>() {

			@Override
			public ContentDataRef transform(SearchHit hit, TemplateWrapper template) {
				return searchHitTx.toContentDataRef(hit, template);
			}
			
		});
	}

	private <T> SearchResult<T> searchAndGetTransformedResult(String text, TemplateWrapper template,
			int pageSize, int pageNum, ResultTx<T> resultTx) {
		
		SearchResponse searchResponse = searchAndGetResponse(text, template, pageSize, pageNum);
		
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

	private SearchResponse searchAndGetResponse(String text, TemplateWrapper template, int pageSize, int pageNum) {
		
		SearchRequest searchRequest = ESQueryBuilder.builder(text, template.getTemplate())
													.withPagination(pageSize, pageNum).build();
		
		ActionFuture<SearchResponse> searchResponseFuture = esClient.search(searchRequest);
		return searchResponseFuture.actionGet();
	}
	
	@Override
	public SearchResult<ContentDataRecord> searchAndGetRecords(String text, TemplateWrapper template, int pageSize, int pageNum) {
		
		return searchAndGetTransformedResult(text, template, pageSize, pageNum, new ResultTx<ContentDataRecord>() {

			@Override
			public ContentDataRecord transform(SearchHit hit, TemplateWrapper template) {
				return searchHitTx.toContentDataRecord(hit, template);
			}
			
		});
	}
	
	private static interface ResultTx<T> {
		T transform(SearchHit hit, TemplateWrapper template);
	}

}
