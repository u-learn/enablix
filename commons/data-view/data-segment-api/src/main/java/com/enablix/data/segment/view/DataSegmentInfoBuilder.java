package com.enablix.data.segment.view;

import com.enablix.core.api.ContentRecord;
import com.enablix.core.api.ContentRecordRef;
import com.enablix.core.domain.segment.DataSegmentInfo;

public interface DataSegmentInfoBuilder {

	DataSegmentInfo build(ContentRecord record);
	
	DataSegmentInfo build(ContentRecordRef dataRef);
	
	DataSegmentInfo build(String contentQId, String recordIdentity);
}
