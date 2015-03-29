package com.enablix.core.mongo.search;

import org.springframework.data.mongodb.core.query.Criteria;

public interface SearchFilter {

	SearchFilter and(SearchFilter andWithFilter);
	
	SearchFilter or(SearchFilter orWithFilter);

	Criteria toPredicate(Criteria root);
	
}
