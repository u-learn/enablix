package com.enablix.analytics.search.es;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;

import com.enablix.analytics.search.SearchClient;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;

public class ElasticSearchClient implements SearchClient {

	@Autowired
	private Client esClient;
	
	@Autowired
	private SearchHitToContentDataRefTransformer searchHitTx;
	
	@Override
	public List<ContentDataRef> search(String text, ContentTemplate template) {
		
		SearchRequest searchRequest = ESQueryBuilder.builder(text, template).build();
		
		ActionFuture<SearchResponse> searchResponseFuture = esClient.search(searchRequest);
		SearchResponse searchResponse = searchResponseFuture.actionGet();
		
		List<ContentDataRef> result = new ArrayList<>();
		
		for (SearchHit hit : searchResponse.getHits()) {
			ContentDataRef contentDataRef = searchHitTx.transform(hit, template);
			if (contentDataRef != null) {
				result.add(contentDataRef);
			}
		}
		
		return result;
	}

}
