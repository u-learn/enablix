package com.enablix.app.content.doc.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.enablix.app.config.ConfigurationCrudService;
import com.enablix.app.service.CrudResponse;
import com.enablix.commons.config.TenantDBConfigurationProvider;
import com.enablix.commons.dms.DMSUtil;
import com.enablix.commons.dms.DocumentStoreConstants;
import com.enablix.commons.util.concurrent.Events;
import com.enablix.core.domain.config.Configuration;
import com.enablix.core.mq.Event;
import com.enablix.core.mq.util.EventUtil;

@RestController
@RequestMapping("docstore")
public class DocStoreConfigController {

	private static final Logger LOGGER = LoggerFactory.getLogger(DocStoreConfigController.class);
	
	@Autowired
	private TenantDBConfigurationProvider configProvider;
	
	@Autowired
	private ConfigurationCrudService configManager;
	
	//@Autowired
	/*private DocStoreConfigMetadataProvider docstoreConfigMetadataProvider;
	
	@RequestMapping(value = "/configmetadata", method = RequestMethod.GET, produces = "application/json")
	public Collection<DocStoreConfigMetadata> fetchDocStoresConfigMetadata() {
		LOGGER.debug("Fetching doc store config metadata");
		return docstoreConfigMetadataProvider.getDocStoresConfigMetadata();
	}*/
	
	@RequestMapping(value = "/default", method = RequestMethod.GET, produces = "application/json")
	public Configuration defaultDocStoreConfig(HttpServletRequest req, HttpServletResponse res) {
		
		LOGGER.debug("Fetching default doc store config");
		
		Configuration docStoreConfig = null;
		
		Configuration defaultDocStoreConfig = configProvider.getConfiguration(DocumentStoreConstants.DOC_STORE_CONFIG_KEY);
		if (defaultDocStoreConfig != null) {
			docStoreConfig = configProvider.getConfiguration(DMSUtil.getDocStoreConfigKey(
				defaultDocStoreConfig.getStringValue(DocumentStoreConstants.DEFAULT_DOC_STORE_CONFIG_PROP)));
		}

		if (docStoreConfig == null) {
			res.setStatus(HttpStatus.SC_NO_CONTENT);
		}
		
		return docStoreConfig;
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/config")
	public Configuration saveConfiguration(@RequestBody Configuration config,
			HttpServletRequest req, HttpServletResponse res) {
		
		// create or update config for default document store
		Configuration defaultDocStoreConfig = new Configuration();
		defaultDocStoreConfig.setKey(DocumentStoreConstants.DOC_STORE_CONFIG_KEY);
		defaultDocStoreConfig.addConfigProperty(DocumentStoreConstants.DEFAULT_DOC_STORE_CONFIG_PROP, 
								config.getStringValue(DocumentStoreConstants.DOC_STORE_TYPE_PROP));

		configManager.saveOrUpdate(defaultDocStoreConfig);
		CrudResponse<Configuration> response = configManager.saveOrUpdate(config);
		
		EventUtil.publishEvent(new Event<Configuration>(Events.DOC_STORE_CONFIG_UPDATED, config));
		
		return response.getPayload();
	}

	
}
