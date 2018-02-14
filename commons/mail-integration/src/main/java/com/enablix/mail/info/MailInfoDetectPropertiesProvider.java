package com.enablix.mail.info;

import org.springframework.stereotype.Component;

import com.enablix.analytics.info.detection.InfoDetectionConfigProvider;
import com.enablix.analytics.info.detection.InfoDetectionConfiguration;
import com.enablix.core.mail.utility.MailConstants;

@Component
public class MailInfoDetectPropertiesProvider implements InfoDetectionConfigProvider {

	@Override
	public InfoDetectionConfiguration getConfiguration() {
		return MailInfoDetectionProperties.getFromConfiguration();
	}

	@Override
	public String infoType() {
		return MailConstants.MAIL_INFO_TYPE;
	}

}
