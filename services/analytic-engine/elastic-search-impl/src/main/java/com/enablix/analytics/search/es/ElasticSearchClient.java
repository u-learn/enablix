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
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.api.SearchResult;
import com.enablix.services.util.template.TemplateWrapper;

public class ElasticSearchClient implements SearchClient {

	@Autowired
	private Client esClient;
	
	@Autowired
	private SearchHitToContentDataRefTransformer searchHitTx;
	
	@Override
	public SearchResult<ContentDataRef> search(String text, TemplateWrapper template, int pageSize, int pageNum) {
		
		SearchRequest searchRequest = ESQueryBuilder.builder(text, template.getTemplate())
													.withPagination(pageSize, pageNum).build();
		
		ActionFuture<SearchResponse> searchResponseFuture = esClient.search(searchRequest);
		SearchResponse searchResponse = searchResponseFuture.actionGet();
		
		List<ContentDataRef> result = new ArrayList<>();
		
		SearchHits hits = searchResponse.getHits();
		for (SearchHit hit : hits) {
			ContentDataRef contentDataRef = searchHitTx.transform(hit, template);
			if (contentDataRef != null) {
				result.add(contentDataRef);
			}
		}
		
		SearchResult<ContentDataRef> searchResult = new SearchResult<>();
		searchResult.setContent(result);
		searchResult.setNumberOfElements(hits.getTotalHits());
		searchResult.setPageSize(pageSize);
		searchResult.setCurrentPage(pageNum);
		searchResult.setTotalPages((long) Math.ceil(((double) hits.getTotalHits())/pageSize));
		
		return searchResult;
	}

}
