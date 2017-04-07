package com.enablix.data.view.mongo;

import java.util.List;

import com.enablix.core.domain.segment.DataSegment;
import com.enablix.data.view.DataSegmentAttrFilter;

public interface CollectionViewBuilder {

	boolean isCollectionVisible(String collectionName, DataSegment dataSegment);

	List<DataSegmentAttrFilter> createDataSegmentFilters(String collectionName, DataSegment dataSegment);
	
	AttributeMatcher<?> attributeMatcher(String collectionName);
	
}
