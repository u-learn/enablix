package com.enablix.es.view;

import org.elasticsearch.index.query.QueryBuilder;

public interface TypeView {

	boolean isVisible();
	
	String typeName();
	
	QueryBuilder baseQuery();
	
}
