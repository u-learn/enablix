package com.enablix.commons.dms.api;

import org.springframework.beans.factory.annotation.Autowired;

import com.enablix.commons.config.ConfigurationProvider;
import com.enablix.commons.dms.DMSUtil;
import com.enablix.core.domain.config.Configuration;

public abstract class AbstractDocumentStore<DM extends DocumentMetadata, D extends Document<DM>> implements DocumentStore<DM, D> {

	@Autowired 
	protected ConfigurationProvider configProvider;
	
	public AbstractDocumentStore() {
		super();
	}

	protected Configuration getDocStoreConfiguration() {
		
		Configuration config = configProvider.getConfiguration(DMSUtil.getDocStoreConfigKey(type()));
		
		if (config == null) {
			throw new IllegalStateException("No configuration found for docstore type [" + type() + "]");
		}
		return config;
	}

}