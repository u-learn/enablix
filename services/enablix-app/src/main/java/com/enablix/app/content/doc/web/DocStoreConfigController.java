package com.enablix.app.content.doc.web;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.enablix.app.config.ConfigurationCrudService;
import com.enablix.app.content.doc.DocStoreConfigMetadata;
import com.enablix.app.content.doc.DocStoreConfigMetadataProvider;
import com.enablix.commons.config.ConfigurationProvider;
import com.enablix.commons.dms.DMSUtil;
import com.enablix.commons.dms.DocumentStoreConstants;
import com.enablix.core.domain.config.Configuration;

@RestController
@RequestMapping("docstore")
public class DocStoreConfigController {

	private static final Logger LOGGER = LoggerFactory.getLogger(DocStoreConfigController.class);
	
	@Autowired
	private ConfigurationProvider configProvider;
	
	@Autowired
	private ConfigurationCrudService configManager;
	
	@Autowired
	private DocStoreConfigMetadataProvider docstoreConfigMetadataProvider;
	
	@RequestMapping(value = "/configmetadata", method = RequestMethod.GET, produces = "application/json")
	public Collection<DocStoreConfigMetadata> fetchDocStoresConfigMetadata() {
		LOGGER.debug("Fetching doc store config metadata");
		return docstoreConfigMetadataProvider.getDocStoresConfigMetadata();
	}
	
	@RequestMapping(value = "/default", method = RequestMethod.GET, produces = "application/json")
	public Configuration defaultDocStoreConfig() {
		
		LOGGER.debug("Fetching default doc store config");
		
		Configuration docStoreConfig = null;
		
		Configuration defaultDocStoreConfig = configProvider.getConfiguration(DocumentStoreConstants.DEFUALT_DOC_STORE_CONFIG_KEY);
		if (defaultDocStoreConfig != null) {
			docStoreConfig = configProvider.getConfiguration(DMSUtil.getDocStoreConfigKey(
				defaultDocStoreConfig.getStringValue(DocumentStoreConstants.DEFUALT_DOC_STORE_CONFIG_PROP)));
		}

		return docStoreConfig;
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/config")
	public void saveConfiguration(@RequestBody Configuration config) {
		
		// create or update config for default document store
		Configuration defaultDocStoreConfig = new Configuration();
		defaultDocStoreConfig.setKey(DocumentStoreConstants.DEFUALT_DOC_STORE_CONFIG_KEY);
		defaultDocStoreConfig.addConfigProperty(DocumentStoreConstants.DEFUALT_DOC_STORE_CONFIG_PROP, 
								config.getStringValue(DocumentStoreConstants.DOC_STORE_TYPE_PROP));

		configManager.saveOrUpdate(defaultDocStoreConfig);
		configManager.saveOrUpdate(config);
	}

	
}
