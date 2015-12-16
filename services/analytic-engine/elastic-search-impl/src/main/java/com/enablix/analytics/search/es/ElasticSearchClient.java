package com.enablix.analytics.search.es;

import java.util.List;

import org.elasticsearch.client.Client;
import org.springframework.beans.factory.annotation.Autowired;

import com.enablix.analytics.search.SearchClient;
import com.enablix.core.api.ContentDataRef;

public class ElasticSearchClient implements SearchClient {

	@Autowired
	private Client esClient;
	
	@Override
	public List<ContentDataRef> search(String text, String templateId) {
		// TODO Auto-generated method stub
		return null;
	}

}
