package com.enablix.wordpress.info.detection;

import org.springframework.stereotype.Component;

import com.enablix.analytics.info.detection.InfoDetectionConfigProvider;
import com.enablix.analytics.info.detection.InfoDetectionConfiguration;
import com.enablix.wordpress.integration.WPIntegrationProperties;
import com.enablix.wordpress.integration.WordpressConstants;

@Component
public class WPInfoDetectionConfigProvider implements InfoDetectionConfigProvider {

	@Override
	public InfoDetectionConfiguration getConfiguration() {
		return WPIntegrationProperties.getFromConfiguration();
	}

	@Override
	public String infoType() {
		return WordpressConstants.INFO_TYPE_WP_POST;
	}

}
