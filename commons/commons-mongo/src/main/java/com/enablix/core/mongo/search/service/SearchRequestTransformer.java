package com.enablix.core.mongo.search.service;

import org.springframework.data.mongodb.core.query.Criteria;

public interface SearchRequestTransformer {

	Criteria buildQueryCriteria(SearchRequest request);
	
}
