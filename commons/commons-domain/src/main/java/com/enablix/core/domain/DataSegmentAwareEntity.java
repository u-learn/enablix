package com.enablix.core.domain;

import org.springframework.data.mongodb.core.mapping.Document;

import com.enablix.core.domain.segment.DataSegmentAware;
import com.enablix.core.domain.segment.DataSegmentInfo;

@Document
public class DataSegmentAwareEntity extends BaseDocumentEntity implements DataSegmentAware {

	private DataSegmentInfo dataSegmentInfo;
	
	@Override
	public DataSegmentInfo getDataSegmentInfo() {
		return dataSegmentInfo;
	}

	public void setDataSegmentInfo(DataSegmentInfo dataSegmentInfo) {
		this.dataSegmentInfo = dataSegmentInfo;
	}

}
