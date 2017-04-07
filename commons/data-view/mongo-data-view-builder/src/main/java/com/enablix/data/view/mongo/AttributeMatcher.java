package com.enablix.data.view.mongo;

import java.util.List;

import com.enablix.core.mongo.search.SearchFilter;
import com.enablix.data.view.DataSegmentAttrFilter;

public interface AttributeMatcher<T> {

	boolean matchAttributes(T record, List<DataSegmentAttrFilter> dsFilters);
	
	SearchFilter attributeFilters(List<DataSegmentAttrFilter> dsFilters);
	
}
