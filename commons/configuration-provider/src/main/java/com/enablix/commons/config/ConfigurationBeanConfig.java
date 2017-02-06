package com.enablix.commons.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigurationBeanConfig {

	@Bean
	public TenantDBConfigurationProvider tenantDBConfigProvider() {
		return new TenantDBConfigurationProvider();
	}
	
	@Bean
	public SystemDBConfigurationProvider systemDBConfigProvider() {
		return new SystemDBConfigurationProvider();
	}
	
	@Bean
	public ConfigurationProviderChain configurationProviderChain() {
		
		ConfigurationProviderChain chain = new ConfigurationProviderChain();
		
		// the order in which the providers are added, is the order in which
		// lookup happens on the providers for a configuration key.
		
		// The tenant DB is looked up first for any configuration
		chain.addProvider(tenantDBConfigProvider());
		
		// System DB configurations act as a second lookup for configuration
		chain.addProvider(systemDBConfigProvider());
		
		ConfigurationUtil.registerProvider(chain);
		
		return chain;
	}
	
}
