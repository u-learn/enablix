package com.enablix.data.view.mongo;

import java.util.List;

import com.enablix.data.view.DataSegmentAttrFilter;

public interface DataSegmentFilterBuilder {

	List<DataSegmentAttrFilter> createFilters(String collectionName);
	
}
