package com.enablix.data.view;

import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.domain.segment.DataSegment;

public interface ContainerVisibilityChecker {

	boolean isContainerVisible(String containerQId, DataSegment dataSegment);
	
	boolean isContainerVisible(ContainerType container, DataSegment dataSegment);
	
}
