package com.enablix.data.view;

import java.util.List;

import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.domain.segment.DataSegment;

public interface ContainerDataSegmentFilterBuilder {

	List<DataSegmentAttrFilter> createFilters(String containerQId, DataSegment dataSegment);
	
	List<DataSegmentAttrFilter> createFilters(ContainerType containerDef, DataSegment dataSegment);
	
}
