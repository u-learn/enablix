package com.enablix.app.config.web;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.enablix.app.config.ConfigurationCrudService;
import com.enablix.app.service.CrudResponse;
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
	public Configuration fetchDocStoresConfigMetadata(HttpServletResponse res, 
			@PathVariable String configKey) {
		
		Configuration configuration = configProvider.getConfiguration(configKey);
		
		if (configuration == null) {
			res.setStatus(HttpStatus.SC_NO_CONTENT);
		}
		
		return configuration;
	}

	@RequestMapping(method = RequestMethod.POST, value="/save")
	public Configuration saveConfiguration(@RequestBody Configuration config) {
		CrudResponse<Configuration> response = configManager.saveOrUpdate(config);
		return response.getPayload();
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/delete/{configIdentity}/")
	public void deleteConfiguration(HttpServletResponse res, 
			@PathVariable String configIdentity) {
		configManager.getRepository().deleteByIdentity(configIdentity);
		res.setStatus(HttpStatus.SC_NO_CONTENT);
	}
	
}
