package com.enablix.app.config.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.enablix.app.config.ConfigurationCrudService;
import com.enablix.commons.config.TenantDBConfigurationProvider;
import com.enablix.core.domain.config.Configuration;

@RestController
@RequestMapping("config")
public class ConfigurationController {

	@Autowired
	private TenantDBConfigurationProvider configProvider;
	
	@Autowired
	private ConfigurationCrudService configManager;
	
	@RequestMapping(value = "/{configKey}", method = RequestMethod.GET, produces = "application/json")
	public Configuration fetchDocStoresConfigMetadata(@PathVariable String configKey) {
		return configProvider.getConfiguration(configKey);
	}

	@RequestMapping(method = RequestMethod.POST, value="/save")
	public void saveConfiguration(@RequestBody Configuration config) {
		configManager.saveOrUpdate(config);
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/delete/{configIdentity}/")
	public void deleteConfiguration(@PathVariable String configIdentity) {
		configManager.getRepository().deleteByIdentity(configIdentity);
	}
	
}
