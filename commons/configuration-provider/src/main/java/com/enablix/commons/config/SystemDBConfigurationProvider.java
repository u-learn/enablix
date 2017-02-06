package com.enablix.commons.config;

import org.springframework.beans.factory.annotation.Autowired;

import com.enablix.core.domain.config.Configuration;
import com.enablix.core.system.repo.SystemConfigurationRepository;

public class SystemDBConfigurationProvider implements ConfigurationProvider {

	@Autowired
	private SystemConfigurationRepository configRepo;

	@Override
	public Configuration getConfiguration(String configKey) {
		return configRepo.findByKey(configKey);
	}
	
}
