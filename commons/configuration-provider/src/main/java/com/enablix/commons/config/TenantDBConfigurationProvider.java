package com.enablix.commons.config;

import org.springframework.beans.factory.annotation.Autowired;

import com.enablix.core.domain.config.Configuration;
import com.enablix.core.mongo.config.repo.ConfigurationRepository;

public class TenantDBConfigurationProvider implements ConfigurationProvider {

	@Autowired
	private ConfigurationRepository configRepo;
	
	public Configuration getConfiguration(String configKey) {
		return configRepo.findByKey(configKey);
	}
	
}
