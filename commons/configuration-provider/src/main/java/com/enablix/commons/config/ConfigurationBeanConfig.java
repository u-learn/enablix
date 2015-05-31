package com.enablix.commons.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigurationBeanConfig {

	@Bean
	public ConfigurationProvider configProvider() {
		return new ConfigurationProviderImpl();
	}
	
	
}
