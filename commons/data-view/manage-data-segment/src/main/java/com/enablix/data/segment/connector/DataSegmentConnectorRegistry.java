package com.enablix.data.segment.connector;

import java.util.Collection;

import com.enablix.data.segment.view.DataSegmentConnector;

public interface DataSegmentConnectorRegistry {

	@SuppressWarnings("rawtypes")
	Collection<DataSegmentConnector> getAllConnectors();
	
}
