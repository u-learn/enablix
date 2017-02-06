package com.enablix.commons.config;

import java.util.ArrayList;
import java.util.List;

import com.enablix.core.domain.config.Configuration;

public class ConfigurationProviderChain implements ConfigurationProvider {

	private List<ConfigurationProvider> chain;
	
	public ConfigurationProviderChain() {
		this.chain = new ArrayList<>();
	}
	
	public void addProvider(ConfigurationProvider provider) {
		this.chain.add(provider);
	}
	
	@Override
	public Configuration getConfiguration(String configKey) {
		
		Configuration config = null;
		
		for (ConfigurationProvider provider : chain) {
			
			config = provider.getConfiguration(configKey);
			
			if (config != null) {
				break;
			}
		}
		
		return config;
	}

}
