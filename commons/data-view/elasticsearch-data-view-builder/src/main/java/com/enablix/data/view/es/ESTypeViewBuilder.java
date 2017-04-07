package com.enablix.data.view.es;

import java.util.List;

import com.enablix.core.domain.segment.DataSegment;
import com.enablix.data.view.DataSegmentAttrFilter;

public interface ESTypeViewBuilder {

	boolean isTypeVisible(String type, DataSegment ds);

	List<DataSegmentAttrFilter> createDataSegmentFilters(String type, DataSegment dataSegment);
	
}
