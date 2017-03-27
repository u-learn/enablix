package com.enablix.data.segment;

import org.springframework.data.mongodb.core.query.Query;

public interface DataSegmentFilter {

	Query addDataSegmentFiltersToQuery(Query inQuery);
	
	boolean hasAccessToCollection(String collectionName);
	
}
