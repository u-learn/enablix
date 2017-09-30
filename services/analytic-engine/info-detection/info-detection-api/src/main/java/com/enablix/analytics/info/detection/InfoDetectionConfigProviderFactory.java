package com.enablix.analytics.info.detection;

public interface InfoDetectionConfigProviderFactory {

	InfoDetectionConfigProvider getConfigProvider(String infoType);
	
}
