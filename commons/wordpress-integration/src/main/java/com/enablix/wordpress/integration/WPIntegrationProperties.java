package com.enablix.wordpress.integration;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enablix.commons.config.ConfigurationUtil;
import com.enablix.core.domain.config.Configuration;

public class WPIntegrationProperties {

	/*
	 * This class is stored in Configuration as a value. So any changes to the property
	 * names would require an update in the mongo database for the corresponding
	 * entry.
	 */
	
	private static final Logger LOGGER = LoggerFactory.getLogger(WPIntegrationProperties.class);
	
	private String defaultContentTypeQId;
	
	// Wordpress Category Id to Container QId mapping 
	private Map<String, String> wpCatToContQId;
	
	private Map<String, String> contQIdToSlugMatchAttrId;
	
	public String getDefaultContentTypeQId() {
		return defaultContentTypeQId;
	}
	
	public void setDefaultContentTypeQId(String defaultContentTypeQId) {
		this.defaultContentTypeQId = defaultContentTypeQId;
	}
	
	public Map<String, String> getWpCatToContQId() {
		return wpCatToContQId;
	}
	
	public void setWpCatToContQId(Map<String, String> wpCatToContQId) {
		this.wpCatToContQId = wpCatToContQId;
	}
	
	public Map<String, String> getContQIdToSlugMatchAttrId() {
		return contQIdToSlugMatchAttrId;
	}
	
	public void setContQIdToSlugMatchAttrId(Map<String, String> contQIdToSlugMatchAttrId) {
		this.contQIdToSlugMatchAttrId = contQIdToSlugMatchAttrId;
	}
	
	
	public static WPIntegrationProperties getFromConfiguration() {
		
		WPIntegrationProperties props = null;
		
		Configuration config = ConfigurationUtil.getConfig(WordpressConstants.WP_INTEGRATION_PROPERTIES_CONFIG_KEY);
		
		if (config != null) {
		
			Object propsObj = config.getConfig().get("properties");
			if (propsObj instanceof WPIntegrationProperties) {
				props = (WPIntegrationProperties) propsObj;
			}
			
		} else {
			LOGGER.warn("Wordpress Integration Properties not found");
		}
		
		return props;
	}
	
}
