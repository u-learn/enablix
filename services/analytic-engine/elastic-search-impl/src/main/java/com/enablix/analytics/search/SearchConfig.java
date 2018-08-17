package com.enablix.analytics.search;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.enablix.analytics.search.es.ElasticSearchClient;

@Component
public class SearchConfig {

	@Bean
	public ElasticSearchClient elasticSearchClient() {
		return new ElasticSearchClient();
	}
	
}
