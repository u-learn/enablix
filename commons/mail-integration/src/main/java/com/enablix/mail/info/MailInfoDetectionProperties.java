package com.enablix.mail.info;

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
import com.enablix.core.mail.utility.MailConstants;

public class MailInfoDetectionProperties implements InfoDetectionConfiguration {

	private static final Logger LOGGER = LoggerFactory.getLogger(MailInfoDetectionProperties.class);
	
	/*
	 * This class is stored in Configuration as a value. So any changes to the property
	 * names would require an update in the mongo database for the corresponding
	 * entry.
	 */
	
	// default container QId to use in case no other resolution strategy works
	private String defaultContentTypeQId;
	
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

	public static MailInfoDetectionProperties getFromConfiguration() {
		
		MailInfoDetectionProperties props = null;
		
		Configuration config = ConfigurationUtil.getConfig(MailConstants.MAIL_INFO_DETECT_PROPERTIES_CONFIG_KEY);
		
		if (config != null) {
		
			Object propsObj = config.getConfig().get("properties");
			if (propsObj instanceof MailInfoDetectionProperties) {
				props = (MailInfoDetectionProperties) propsObj;
				initTagAliases(props);
			}
			
		} else {
			LOGGER.warn("Mail Info Detect Properties not found");
		}
		
		return props;
	}

	private static void initTagAliases(MailInfoDetectionProperties props) {
		
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
