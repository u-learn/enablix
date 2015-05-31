package com.enablix.commons.config;

import com.enablix.core.domain.config.Configuration;

public class ConfigurationUtil {

	private static ConfigurationProvider CONFIG_PROVIDER;
	
	public static void registerProvider(ConfigurationProvider provider) {
		CONFIG_PROVIDER = provider;
	}
	
	public static Configuration getConfig(String configKey) {
		return CONFIG_PROVIDER.getConfiguration(configKey);
	}
	
}
