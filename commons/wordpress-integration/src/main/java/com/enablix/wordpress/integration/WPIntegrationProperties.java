package com.enablix.wordpress.integration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enablix.analytics.info.detection.InfoDetectionConfiguration;
import com.enablix.analytics.info.detection.InfoTag;
import com.enablix.analytics.info.detection.SimpleInfoTag;
import com.enablix.commons.config.ConfigurationUtil;
import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.core.domain.config.Configuration;

public class WPIntegrationProperties implements InfoDetectionConfiguration {

	private static final Logger LOGGER = LoggerFactory.getLogger(WPIntegrationProperties.class);
	
	/*
	 * This class is stored in Configuration as a value. So any changes to the property
	 * names would require an update in the mongo database for the corresponding
	 * entry.
	 */
	
	// default container QId to use in case no other resolution strategy works
	private String defaultContentTypeQId;
	
	// Wordpress Category Id to Container QId mapping to identify the enablix type
	// based on the category of the post
	private Map<String, String> wpCatToContQId;
	
	// Container QId to its attribute which should be looked-up to match post-slug
	private Map<String, String> contQIdToSlugMatchAttrId;
	
	// Wordpress Post url pattern to Container QId mapping to identify the enablix type
	// based on the url of the post 
	private Map<String, String> linkPatternToContQId;
	
	// Default filters to use in PostFeederTask
	private Map<String, List<String>> taskDefaultFilters;
	
	private Map<String, List<String>> tagAliasMapping;
	
	private boolean saveAsDraft;

	/* ************ End of properties defined in mongo database ***************** */
	
	
	private Map<String, List<InfoTag>> tagAliases;
	
	@Override
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
	
	@Override
	public Map<String, String> getLinkPatternToContQId() {
		return linkPatternToContQId;
	}

	public void setLinkPatternToContQId(Map<String, String> linkPatternToContQId) {
		this.linkPatternToContQId = linkPatternToContQId;
	}
	
	public Map<String, List<String>> getTaskDefaultFilters() {
		return taskDefaultFilters;
	}

	public void setTaskDefaultFilters(Map<String, List<String>> defaultFilters) {
		this.taskDefaultFilters = defaultFilters;
	}

	public Map<String, List<String>> getTagAliasMapping() {
		return tagAliasMapping;
	}

	public void setTagAliasMapping(Map<String, List<String>> tagAliasMapping) {
		this.tagAliasMapping = tagAliasMapping;
	}

	@Override
	public Map<String, List<InfoTag>> getTagAliases() {
		return tagAliases;
	}
	
	@Override
	public boolean isSaveAsDraft() {
		return saveAsDraft;
	}

	public void setSaveAsDraft(boolean saveAsDraft) {
		this.saveAsDraft = saveAsDraft;
	}

	public static WPIntegrationProperties getFromConfiguration() {
		
		WPIntegrationProperties props = null;
		
		Configuration config = ConfigurationUtil.getConfig(WordpressConstants.WP_INTEGRATION_PROPERTIES_CONFIG_KEY);
		
		if (config != null) {
		
			Object propsObj = config.getConfig().get("properties");
			if (propsObj instanceof WPIntegrationProperties) {
				props = (WPIntegrationProperties) propsObj;
				initTagAliases(props);
			}
			
		} else {
			LOGGER.warn("Wordpress Integration Properties not found");
		}
		
		return props;
	}

	private static void initTagAliases(WPIntegrationProperties props) {
		
		props.tagAliases = new HashMap<String, List<InfoTag>>();
		
		if (props.tagAliasMapping != null) {
			
			props.tagAliasMapping.entrySet().forEach((entry) -> {
				props.tagAliases.put(entry.getKey(), 
						CollectionUtil.transform(entry.getValue(), 
								() -> new ArrayList<InfoTag>(), 
								(tagName) -> new SimpleInfoTag(tagName)));
			});
		}
	}
	
}
