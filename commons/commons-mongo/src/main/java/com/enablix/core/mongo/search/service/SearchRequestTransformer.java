package com.enablix.core.mongo.search.service;

import org.springframework.data.mongodb.core.query.Criteria;

import com.enablix.core.api.SearchRequest;

public interface SearchRequestTransformer {

	Criteria buildQueryCriteria(SearchRequest request);
	
}
