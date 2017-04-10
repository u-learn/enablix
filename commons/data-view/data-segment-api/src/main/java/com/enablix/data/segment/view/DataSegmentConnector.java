package com.enablix.data.segment.view;

import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.core.commons.xsdtopojo.DataSegmentDefinitionType;
import com.enablix.core.domain.segment.DataSegmentAware;
import com.enablix.core.domain.segment.DataSegmentInfo;

public interface DataSegmentConnector<T extends DataSegmentAware> {

	DataSegmentInfo buildDataSegmentInfo(T content);
	
	Class<T> connectorFor();

	void updateAllRecords(ContentTemplate template, DataSegmentDefinitionType dataSegmentDef);
	
}
