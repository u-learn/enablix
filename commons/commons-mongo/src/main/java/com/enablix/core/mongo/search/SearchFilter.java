package com.enablix.core.mongo.search;

import org.springframework.data.mongodb.core.query.Criteria;

public interface SearchFilter {

	Criteria toPredicate();
	
	SearchFilter and(SearchFilter andWithFilter);
	
	SearchFilter or(SearchFilter orWithFilter);
	
}
