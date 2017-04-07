package com.enablix.data.view.mongo.coll;

import java.util.List;

import com.enablix.core.domain.segment.DataSegment;
import com.enablix.data.view.DataSegmentAttrFilter;
import com.enablix.data.view.mongo.AttributeMatcher;

public interface CollectionViewBuilderHelper {

	boolean isCollectionVisible(String collectionName, DataSegment dataSegment);
	
	boolean canHandle(String collectionName);

	List<DataSegmentAttrFilter> createDataSegmentFilters(String collectionName, DataSegment dataSegment);

	AttributeMatcher<?> attributeMatcher();
	
}
