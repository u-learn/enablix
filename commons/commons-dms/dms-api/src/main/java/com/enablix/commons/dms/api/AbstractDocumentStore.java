package com.enablix.commons.dms.api;

import com.enablix.commons.config.ConfigurationUtil;
import com.enablix.commons.dms.DMSUtil;
import com.enablix.core.domain.config.Configuration;

public abstract class AbstractDocumentStore<DM extends DocumentMetadata, D extends Document<DM>> implements DocumentStore<DM, D> {

	public AbstractDocumentStore() {
		super();
	}

	protected Configuration getDocStoreConfiguration() {
		
		Configuration config = ConfigurationUtil.getConfig(DMSUtil.getDocStoreConfigKey(type()));
		
		if (config == null) {
			throw new IllegalStateException("No configuration found for docstore type [" + type() + "]");
		}
		return config;
	}

	
}