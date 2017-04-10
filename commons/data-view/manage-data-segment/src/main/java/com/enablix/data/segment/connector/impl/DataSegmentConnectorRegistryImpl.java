package com.enablix.data.segment.connector.impl;

import java.util.Collection;

import org.springframework.stereotype.Component;

import com.enablix.commons.util.beans.SpringBackedAbstractFactory;
import com.enablix.data.segment.connector.DataSegmentConnectorRegistry;
import com.enablix.data.segment.view.DataSegmentConnector;

@SuppressWarnings("rawtypes")
@Component
public class DataSegmentConnectorRegistryImpl extends SpringBackedAbstractFactory<DataSegmentConnector> implements DataSegmentConnectorRegistry {

	@Override
	public Collection<DataSegmentConnector> getAllConnectors() {
		return registeredInstances();
	}

	@Override
	protected Class<DataSegmentConnector> lookupForType() {
		return DataSegmentConnector.class;
	}

}
