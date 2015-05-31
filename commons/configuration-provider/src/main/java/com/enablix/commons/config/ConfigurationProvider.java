package com.enablix.commons.config;

import com.enablix.core.domain.config.Configuration;

public interface ConfigurationProvider {

	Configuration getConfiguration(String configKey);
	
}
