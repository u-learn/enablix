package com.enablix.state.change.model;

import com.enablix.core.domain.segment.DataSegmentAware;
import com.enablix.core.domain.segment.DataSegmentInfo;

public class DataSegmentAwareStateChangeRecording<T extends RefObject> extends StateChangeRecording<T> implements DataSegmentAware {

	private DataSegmentInfo dataSegmentInfo;
	
	@Override
	public DataSegmentInfo getDataSegmentInfo() {
		return dataSegmentInfo;
	}

	public void setDataSegmentInfo(DataSegmentInfo dataSegmentInfo) {
		this.dataSegmentInfo = dataSegmentInfo;
	}

}
